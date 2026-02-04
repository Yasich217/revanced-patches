package app.revanced.extension.youtube.patches;

import android.widget.Toast;

import app.revanced.extension.shared.Logger;
import app.revanced.extension.shared.Utils;
import app.revanced.extension.youtube.patches.VideoInformation;
import app.revanced.extension.youtube.shared.VideoState;

/**
 * Patch для Voice Over Translation
 * Отслеживает изменение videoId и управляет воспроизведением аудио перевода
 * 
 * Использует существующие хуки ReVanced:
 * - VideoInformation для получения информации о видео
 * - VideoState для отслеживания состояния воспроизведения
 */
public class VoiceOverTranslationPatch {
    private static String currentVideoId = null;
    private static TranslationData currentTranslation = null;
    private static long lastToastTime = 0;
    private static final long TOAST_INTERVAL_MS = 10000; // Показывать toast каждые 10 секунд

    /**
     * Вызывается при изменении videoId через hookVideoId
     * 
     * @param videoId ID нового видео
     */
    public static void onVideoIdChanged(String videoId) {
        if (videoId == null || videoId.equals(currentVideoId)) {
            return;
        }

        Logger.printDebug(() -> "Video ID changed: " + videoId);
        
        currentVideoId = videoId;
        currentTranslation = null;

        // Показываем toast о загрузке перевода
        showToast("Загружаем перевод для видео: " + videoId);

        // TODO: В реальной реализации здесь будет:
        // 1. GET запрос на https://vot.example.com/translation?videoId={videoId}
        // 2. Парсинг JSON ответа
        // 3. Сохранение TranslationData с audioUrl
    }

    /**
     * Вызывается через videoTimeHook примерно раз в секунду
     * 
     * @param videoTime Текущая позиция видео в миллисекундах
     */
    public static void onVideoTimeChanged(long videoTime) {
        if (currentVideoId == null) {
            return;
        }

        VideoState currentState = VideoState.getCurrent();
        long videoLength = VideoInformation.getVideoLength();

        // Проверяем состояние воспроизведения
        if (currentState == VideoState.PLAYING) {
            // Показываем toast периодически
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastToastTime >= TOAST_INTERVAL_MS) {
                String positionStr = formatTime(videoTime);
                String durationStr = formatTime(videoLength);
                showToast("Воспроизводим поток с " + positionStr + "/" + durationStr);
                lastToastTime = currentTime;
            }

            // TODO: В реальной реализации здесь будет:
            // 1. Синхронизация аудио потока с позицией видео
            // 2. Проверка необходимости перемотки/паузы
        } else if (currentState == VideoState.PAUSED) {
            // Показываем toast при паузе (только один раз)
            if (lastToastTime != 0) {
                showToast("Воспроизведение приостановлено");
                lastToastTime = 0;
            }
        } else if (currentState == VideoState.ENDED) {
            // Проверяем, достигли ли конца видео
            if (videoTime >= videoLength - 1000) { // За 1 секунду до конца
                showToast("Воспроизведение завершено, перематываем и воспроизводим с начала");
                
                // TODO: В реальной реализации здесь будет:
                // 1. Остановка аудио потока
                // 2. Перемотка видео на начало через VideoInformation.seekTo(0)
                // 3. Перезапуск воспроизведения
            }
        }
    }

    /**
     * Форматирует время в формат MM:SS
     */
    private static String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Показывает toast сообщение
     */
    private static void showToast(String message) {
        try {
            Utils.showToastShort(message);
        } catch (Exception e) {
            Logger.printException(() -> "Failed to show toast", e);
        }
    }

    /**
     * Класс для хранения данных перевода
     */
    public static class TranslationData {
        public String videoId;
        public String audioUrl;
        public String language;
        public long duration;

        public TranslationData(String videoId, String audioUrl, String language, long duration) {
            this.videoId = videoId;
            this.audioUrl = audioUrl;
            this.language = language;
            this.duration = duration;
        }
    }
}
