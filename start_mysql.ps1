# Script to ensure MySQL Server is running for the Java JDBC project

$existingProcess = Get-Process -Name mysqld -ErrorAction SilentlyContinue

if ($existingProcess) {

    Write-Host "[OK] MySQL server is already running (PID: $($existingProcess.Id))." -ForegroundColor Green

} else {

    Write-Host "Starting MySQL server..." -ForegroundColor Cyan

    $mysqlBin = "C:\Program Files\MySQL\MySQL Server 8.4\bin\mysqld.exe"

    $iniPath = Join-Path $PSScriptRoot "mysql\my.ini"

    if (-not (Test-Path $mysqlBin)) {
        Write-Error "MySQL executable not found at: $mysqlBin"
        exit 1
    }

    Start-Process -FilePath $mysqlBin -ArgumentList "--defaults-file=`"$iniPath`"", "--console" -WindowStyle Minimized

    # Wait until MySQL is ready to accept connections

    $maxAttempts = 15
    $attempt = 0
    $ready = $false

    while ($attempt -lt $maxAttempts) {

        Start-Sleep -Seconds 1
        $attempt++

        $conn = Get-NetTCPConnection -LocalPort 3306 -State Listen -ErrorAction SilentlyContinue

        if ($conn) {
            $ready = $true
            break
        }
    }

    if ($ready) {

        Write-Host "[OK] MySQL server is ready on port 3306!" -ForegroundColor Green

    } else {

        Write-Host "[Warning] MySQL process launched. Check if it's listening on port 3306." -ForegroundColor Yellow

    }
}
