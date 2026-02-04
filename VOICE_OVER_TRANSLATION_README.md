# Voice Over Translation Patch

Патч добавлен в репозиторий и готов к использованию.

## Расположение файлов

- **Kotlin патч**: `patches/src/main/kotlin/app/revanced/patches/youtube/video/voiceovertranslation/VoiceOverTranslationPatch.kt`
- **Java extension**: `extensions/youtube/src/main/java/app/revanced/extension/youtube/patches/VoiceOverTranslationPatch.java`

## Как использовать

1. **Соберите репозиторий**:
   ```bash
   cd revanced-patches
   ./gradlew build
   ```

2. **Запушите изменения в ваш форк**:
   ```bash
   git add .
   git commit -m "Add Voice Over Translation patch"
   git push
   ```

3. **В ReVanced Manager**:
   - Откройте настройки
   - Найдите "Patches source" или "Источник патчей"
   - Измените на URL вашего репозитория (например: `https://github.com/ваш-username/revanced-patches`)
   - Обновите список патчей
   - Патч "Voice Over Translation" появится в списке доступных патчей

## Текущая реализация

Патч показывает Toast'ы для отладки:
- При изменении videoId: "Загружаем перевод для видео: {videoId}"
- При воспроизведении: "Воспроизводим поток с {позиция}/{длительность}"
- При паузе: "Воспроизведение приостановлено"
- При завершении: "Воспроизведение завершено, перематываем и воспроизводим с начала"

## Следующие шаги

Для полной реализации нужно добавить:
1. Сетевые запросы (GET на сервер)
2. Воспроизведение аудио потока
3. Синхронизацию с видео
