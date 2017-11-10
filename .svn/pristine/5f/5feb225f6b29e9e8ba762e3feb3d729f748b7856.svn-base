@echo off  
for /f "delims=" %%i in ('dir /b /a-d /s "*.thrift"') do thrift-0.9.1.exe -gen java "%%i"  
pause 