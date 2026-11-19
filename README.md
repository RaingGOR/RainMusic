# RainMusic

Локальный веб-плеер для MP3 на Windows.

## Запуск

Установите JDK 25 и откройте PowerShell в корне проекта. Укажите путь к одному MP3 и запустите приложение через Gradle Wrapper.

```powershell
$env:RAINMUSIC_MUSIC_PATH = 'C:\Music\track.mp3'
.\gradlew.bat bootRun
```

Откройте `http://localhost:8000/`. Если задан `SERVER_PORT`, используйте этот порт. Чтобы выбрать другой файл, остановите приложение, измените `RAINMUSIC_MUSIC_PATH` и запустите его снова.

## Проверка перемотки

Запустите трек на странице и перемотайте вперёд и назад. Чтобы проверить HTTP Range отдельно, выполните в другом окне PowerShell следующую команду с MP3 размером больше 1024 байт.

```powershell
curl.exe -sS -D - -H "Range: bytes=0-1023" -o NUL -w "Received: %{size_download} bytes\n" http://localhost:8000/api/music
```

Ожидается `206 Partial Content`, `Content-Range: bytes 0-1023/N`, где `N` — размер файла, и 1024 полученных байта.
