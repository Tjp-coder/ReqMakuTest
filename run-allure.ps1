# --- 配置部分 ---
$ProjectRoot = $PSScriptRoot
$SourceDir = Join-Path $ProjectRoot "target\site\allure-maven-plugin"
$DestDir = Join-Path $ProjectRoot "allure-report"

Write-Host "===================================================" -ForegroundColor Gray
Write-Host " Starting Allure Report Deployment Script..." -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Gray

# --- 步骤 1: 运行测试 ---
Write-Host "[1/3] Running Maven tests..." -ForegroundColor Cyan
mvn clean test "-Denv=dev"

if ($LASTEXITCODE -ne 0) {
    Write-Host "⚠️ Tests failed, but proceeding to generate report..." -ForegroundColor Yellow
}

# --- 步骤 2: 生成报告 ---
Write-Host "[2/3] Generating Allure report..." -ForegroundColor Cyan
mvn allure:report

if (-not (Test-Path $SourceDir)) {
    Write-Host "❌ ERROR: Allure report was not generated at $SourceDir" -ForegroundColor Red
    exit 1
}

# --- 步骤 3: 清理并复制 (关键修复步骤) ---
Write-Host "[3/3] Cleaning and copying files to root..." -ForegroundColor Cyan

# 1. 如果目标文件夹存在，彻底删除它 (确保干净)
if (Test-Path $DestDir) {
    Remove-Item -Recurse -Force $DestDir
}

# 2. 创建一个新的、空的目标文件夹
New-Item -ItemType Directory -Path $DestDir | Out-Null

# 3. 复制源文件夹内的【所有子项】到目标文件夹
# 注意：使用 \* 表示复制内容，而不是复制文件夹本身
Copy-Item -Path "$SourceDir\*" -Destination $DestDir -Recurse -Force

# --- 验证结果 ---
$FileCount = (Get-ChildItem -Path $DestDir -Recurse -File).Count
Write-Host "===================================================" -ForegroundColor Green
Write-Host "✅ SUCCESS! Copied $FileCount files to './allure-report'" -ForegroundColor Green
Write-Host "===================================================" -ForegroundColor Green

# 检查是否包含 index.html，这是报告存在的标志
if (Test-Path (Join-Path $DestDir "index.html")) {
    Write-Host "📄 Found index.html. Ready for Git." -ForegroundColor White
} else {
    Write-Host "️ Warning: index.html not found in destination!" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Next Steps:" -ForegroundColor Cyan
Write-Host "1. git add allure-report" -ForegroundColor White
Write-Host "2. git commit -m `"docs: update allure report`"" -ForegroundColor White
Write-Host "3. git push origin main" -ForegroundColor White