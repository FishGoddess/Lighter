rem ******************************
rem Lighter server
rem author Fish
rem https://www.fishin.com.cn
rem ******************************

rem 1. find config.properties in ../conf

cd ../conf/
@echo off
set configFile=%cd%/config.properties

rem 2. startup the java class

@echo off
cd %~dp0/classes/

@echo off
call java cn.com.fishin.lighter.Startup %configFile%

:end
