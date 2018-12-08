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
}

function -print-usage {
    cat <<EOU | $fmt
Usage: $progname [-c|--color|--no-color] [-d|--debug] [-h|--help] [-n|--dry-run] [-v|--verbose] [${tasks[@]}]
EOU
}

function -print-help {
    echo "$progname, version $version"
    -print-usage
    cat <<EOH

Flags:
  -c, --color     Print in color
      --no-color  Print without color
  -d, --debug     Print debug output while running
  -h, --help      Print help and exit normally
  -n, --dry-run   Do nothing (dry run); echo actions
  -v, --verbose   Verbose output

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

function db {
    local quiet=-q
    if $debug
    then
        quiet=
    fi
    local rlwrap=rlwrap
    if ! which rlwrap >/dev/null 2>&1
    then
        rlwrap=
    fi

    local maven_repo="$(./mvnw $quiet -DforceStdout help:evaluate -Dexpression=settings.localRepository)"
    case "$(uname)" in
    CYGWIN* | MINGW* ) maven_repo="$(cygpath -m "$maven_repo")" ;;
    esac

    exec $rlwrap java \
        -cp "$maven_repo/org/hsqldb/sqltool/2.4.1/sqltool-2.4.1.jar" \
        org.hsqldb.cmdline.SqlTool \
        --rcFile ./.sqltool.rc
}

function -db-help {
    cat <<EOH
Opens the DB in a command-line.
EOH
}

readonly tasks=($(declare -F | cut -d' ' -f3 | grep -v '^-' | sort))

-setup-terminal

[[ -t 1 ]] && color=true || color=false
let debug=0 || true
print=echo
pwd=pwd
verbose=false
while getopts :-: opt
do
    [[ - == $opt ]] && opt=${OPTARG%%=*} OPTARG=${OPTARG#*=}
    case $opt in
    c | color ) color=true ;;
    no-color ) color=false ;;
    d | debug ) let ++debug ;;
    h | help ) -print-help ; exit 0 ;;
    n | dry-run ) print="echo $print" pwd="echo $pwd" ;;
    v | verbose ) verbose=true ;;
    * ) -print-usage >&2 ; exit 2 ;;
    esac
done
shift $((OPTIND - 1))
readonly print
readonly verbose

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

"$@"
