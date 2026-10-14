$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$localRepository = Join-Path $projectRoot '.tools/m2'
$repositoryArgument = "-Dmaven.repo.local=$localRepository"
$mavenCommand = Get-Command mvn.cmd -ErrorAction SilentlyContinue
if ($mavenCommand) { & $mavenCommand.Source $repositoryArgument @args; exit $LASTEXITCODE }
$cacheRoot = Join-Path ([Environment]::GetFolderPath('UserProfile')) '.m2/wrapper/dists'
$cachedMaven = Get-ChildItem -LiteralPath $cacheRoot -Filter mvn.cmd -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1
if ($cachedMaven) { & $cachedMaven.FullName $repositoryArgument @args; exit $LASTEXITCODE }
$version = '3.9.11'
$toolsRoot = Join-Path $projectRoot '.tools'
$mavenBin = Join-Path $toolsRoot "apache-maven-$version/bin/mvn.cmd"
if (!(Test-Path -LiteralPath $mavenBin)) {
 New-Item -ItemType Directory -Force -Path $toolsRoot | Out-Null
 $archive = Join-Path $toolsRoot 'maven.zip'
 $url = "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$version/apache-maven-$version-bin.zip"
 Invoke-WebRequest -Uri $url -OutFile $archive
 $expected = (Invoke-RestMethod -Uri "$url.sha512").ToString().Trim().Split(' ')[0]
 if ((Get-FileHash -LiteralPath $archive -Algorithm SHA512).Hash.ToLowerInvariant() -ne $expected.ToLowerInvariant()) { throw 'Checksum de Maven incorrecto' }
 Expand-Archive -LiteralPath $archive -DestinationPath $toolsRoot -Force
}
& $mavenBin $repositoryArgument @args
exit $LASTEXITCODE
