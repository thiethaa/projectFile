REM %__CD__% = root Directory of the project
@echo off
echo Resigning licenses...

REM FOR %%X in ("%__CD__%*.properties")DO call gradle signFile -PfileType=license -PpropFile=%%~nxX --warning-mode=none
REM echo on

REM ------------------
REM ----------------------------
REM ----------------------------------------
REM --------------------------------------------------------
REM -------------------------------------------------------------------

echo Moving licenses to the destination Directory....

mkdir "releaseLicenses"
set "destinationdir=%__CD__%\\releaseLicenses"
FOR %%X in ("%__CD__%*.properties")DO move "%%~nxX" "%destinationdir%"

echo on
exit