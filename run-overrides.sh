#!/usr/bin/env bash

export PS4='+${BASH_SOURCE}:${LINENO}:${FUNCNAME[0]:+${FUNCNAME[0]}():} '

set -e
set -u
set -o pipefail

readonly progname="${0##*/}"
readonly version=0

fmt=fmt
function -setup-terminal {
    if [[ ! -t 1 ]]
    then
        readonly fmt
        return 0
    fi

    : "${LINES:=$(tput lines)}"
    export LINES
    : "${COLUMNS:=$(tput cols)}"
    export COLUMNS

    local -r fmt_width=$((COLUMNS - 5))
    if (( fmt_width < 10 ))
    then
        echo "$progname: Your terminal is too narrow." >&2
        exit 2
    fi
    fmt="fmt -w $fmt_width"
    readonly fmt
}

function -setup-colors {
    local -r ncolors=$(tput colors)

    if $color && (( ${ncolors-0} > 7 ))
    then
        printf -v pgreen "$(tput setaf 2)"
        printf -v preset "$(tput sgr0)"
    else
        pgreen=''
        preset=''
    fi
    readonly pgreen
    readonly preset
}

function -maybe-debug {
    case $debug in
    0 ) debug=false ;;
    1 ) debug=true ;;
    * ) debug=true ; set -x ;;
    esac

    if $debug
    then
        verbose=true
    fi
}

function -print-usage {
    cat <<EOU | $fmt
Usage: $progname [-c|--color|--no-color] [-d|--debug] [-h|--help] [-n|--dry-run] [-u|--base-url URL] [-v|--verbose] [${tasks[@]}]
EOU
}

function -print-help {
    echo "$progname, version $version"
    -print-usage
    cat <<EOH

Flags:
  -c, --color          Print in color
      --no-color       Print without color
  -d, --debug          Print debug output while running
  -h, --help           Print help and exit normally
  -n, --dry-run        Do nothing (dry run); echo actions
  -u, --base-url URL   Sets base URL for running app
  -v, --verbose        Verbose output

Tasks:
EOH

    for task in "${tasks[@]}"
    do
        local help_fn="-$task-help"
        echo "  * $task"
        if declare -F -- $help_fn >/dev/null 2>&1
        then
            $help_fn | -format-help
        fi
    done
}

function -format-help {
   $fmt | sed 's/^/       /'
}

function db-reset {
    local quiet=-q
    if $verbose
    then
        quiet=-Dorg.slf4j.simpleLogger.defaultLogLevel=INFO
    fi

    $run ./mvnw $quiet clean

    db-update
}

function -db-reset-help {
    cat <<EOH
Recreates database to current schema.
EOH
}

function db-update {
    local quiet=-q
    if $verbose
    then
        quiet=-Dorg.slf4j.simpleLogger.defaultLogLevel=INFO
    fi

    $run ./mvnw $quiet flyway:migrate
}

function -db-update-help {
    cat <<EOH
Updates database to current schema.
EOH
}

function db-shell {
    local quiet=-q
    if $verbose
    then
        quiet=
    fi
    local rlwrap=rlwrap
    if ! which rlwrap >/dev/null 2>&1
    then
        rlwrap=
    fi

    local sep=
    local maven_repo="$(./mvnw $quiet -DforceStdout help:evaluate -Dexpression=settings.localRepository)"
    case "$(uname)" in
    CYGWIN* | MINGW* )
        maven_repo="$(cygpath -m "$maven_repo")"
        sep=';'
        ;;
    * )
        sep=':'
        ;;
    esac

    declare -a opts
    declare -a args
    for arg in "$@"
    do
        shift
        case $arg in
        -- ) break ;;
        -* ) opts=(${opts[@]+"${opts[@]}"} "$arg") ;;
        * ) args=(${args[@]+"${args[@]}"} "$arg") ;;
        esac
    done
    args=(${args[@]+"${args[@]}"} "$@")

    $run $rlwrap java \
        -cp "$maven_repo/org/hsqldb/hsqldb/2.4.1/hsqldb-2.4.1.jar$sep$maven_repo/org/hsqldb/sqltool/2.4.1/sqltool-2.4.1.jar" \
        org.hsqldb.cmdline.SqlTool \
        --rcFile ./.sqltool.rc \
        "${opts[@]}" \
        overrides \
        "${args[@]}"
}

function -db-shell-help {
    cat <<EOH
Opens the DB in a command-line.  If available, uses 'rlwrap'.
EOH
}

function app-stop {
    local quiet='-sSo /dev/null'
    if $verbose
    then
        quiet=''
    fi

    $run curl -X POST $quiet $base_url/actuator/shutdown
}

function -app-stop-help {
    cat <<EOH
Stops running app on $base_url.
EOH
}

readonly tasks=($(declare -F | cut -d' ' -f3 | grep -v '^-' | sort))

-setup-terminal

base_url=http://localhost:8080
[[ -t 1 ]] && color=true || color=false
let debug=0 || true
run=''
verbose=false
while getopts :cdhnu:v-: opt
do
    [[ - == $opt ]] && opt=${OPTARG%%=*} OPTARG=${OPTARG#*=}
    case $opt in
    c | color ) color=true ;;
    no-color ) color=false ;;
    d | debug ) let ++debug ;;
    h | help ) -print-help ; exit 0 ;;
    n | dry-run ) run=echo ;;
    u | base-url ) base_url=$OPTARG ;;
    v | verbose ) verbose=true ;;
    * ) -print-usage >&2 ; exit 2 ;;
    esac
done
shift $((OPTIND - 1))
readonly run

case $# in
0 ) -print-help ; exit 0 ;;
* ) # TODO: This is ugly code
    cmd="$1"
    found=false
    for task in "${tasks[@]}"
    do
        [[ "$cmd" == "$task" ]] && found=true
    done

    if ! $found
    then
        echo "$progname: $cmd: Unknown command." >&2
        echo "Try '$progname --help' for more information." >&2
        -print-usage >&2
        exit 2
    fi
    ;;
esac

-setup-colors
-maybe-debug
readonly debug
readonly verbose

"$@"
