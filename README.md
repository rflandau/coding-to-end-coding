# coding-to-end-coding
Coding to End Coding (CTEC) is a visual script builder, built to be easy to use and highly extendable.

By default, it supports bash output and a short list of bash commands (see below). However, it has support for any interpreter and single-line command.

## Usage
See the releases sidebar for executables. Download, unzip, read README.txt.

Basically, just run the script appropriate to your OS; the scripts will do the rest.
*Linux: `./run.sh`
*Windows: Run `run.bat`

None of us have macs, so it is unsupported (and Windows is barely supported, seeing as the outputted bash scripts won't run on windows anyways because it is *Bash*).


If you have trouble, try compiling from source (only supported on Linux, see below). If you encounter a bug, please see **Known Bugs** below to see if it is listed.

## Compiling From Source
Both `./compile.sh` and `make` access the included libraries and resources and output to *classes/*.

`./run` will execute the program, but only after one of the above commands.

For example: `./compile && ./run`.

All paths are relative and all necessary libraries are included. The only thing you need is Java >=11.

## Defaults and Extending CTEC
"commands/bash.ctecblock" is the default file used by the program to populate available commands and select interpreter.
This file contains the bash syntax (and therefore CTEC support) for:
* echo
* mv
* ls
* rm 
* cp
* cd
* grep
* cat
* touch

## Documentation
[Style Guide](https://docs.google.com/document/d/1vO9wZONKntIHUPtuKVLrR43201fxKZOYlkU82RzcehE/edit?usp=sharing)

[Known Bugs](https://docs.google.com/document/d/1gVv-_4poZkswRS7VTbn9YDbr1kC2cDXKJR4W0PTj-yI/edit?usp=sharing)


[GUI Layout](https://docs.google.com/document/d/1nJXFnGA7ZT1U0jLA93Ud5Ia_zeOiYF18qkP3r3GW1OI/edit)

[File Structure](https://docs.google.com/document/d/1OfuLw8bSuVBx8LBdxgddJxexmjMsydnI5Qu-rmGp-w8/edit)

[Commands and CommandBlocks](https://docs.google.com/document/d/1xiC8Gcv47KmBzK0JQ92g-MwAtZu2SErB5NA5h_ScBoQ/edit?usp=sharing)

[Vertical Sorting Pane](https://docs.google.com/document/d/1dwoVpiYtEuCVATSe9LvAaxCd3GmpkhMSHjXVB7UGFeI/edit?usp=sharing)


## Upcoming
Our scrum board is on VivifyScrum which does not appear to allow public boards. If you need access, please contact any one of us.
