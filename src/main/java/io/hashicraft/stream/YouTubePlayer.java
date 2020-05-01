package io.hashicraft.stream;

import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.binding.LibVlc;
import com.sun.jna.NativeLibrary;
import java.awt.Canvas;
/**
 * A minimal YouTube player.
 * <p>
 * The URL/MRL must be in the following format:
 *
 * <pre>
 *   http://www.youtube.com/watch?v=000000
 * </pre>
 *
 * The only thing that makes this different from a 'regular' media player
 * application is the following piece of code:
 *
 * <pre>
 * mediaPlayer.setPlaySubItems(true); // &lt;--- This is very important for YouTube media
 * </pre>
 *
 * Note that it is also possible to programmatically play the sub-item in
 * response to events - this is slightly more complex but more flexible.
 * <p>
 * The YouTube web page format changes from time to time. This means that the
 * lua scripts that vlc provides to parse the YouTube web pages when looking for
 * the media to stream may not work. If you get errors, especially errors
 * alluding to malformed urls, then you may need to update your vlc version to
 * get newer lua scripts.
 */
public class YouTubePlayer {

  private final MediaPlayerFactory factory;
  private final EmbeddedMediaPlayer mediaPlayer;

  private static final String DUMP_NATIVE_MEMORY = "false";
  private static final Logger LOGGER = LogManager.getLogger();
  private static final String NATIVE_LIBRARY_SEARCH_PATH = "C:/Program Files/VideoLAN/VLC";

    public YouTubePlayer() {

      if(NATIVE_LIBRARY_SEARCH_PATH != null) {
        LOGGER.info("Explicitly adding JNA native library search path: '{}'", NATIVE_LIBRARY_SEARCH_PATH);
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
      }

      boolean found = new NativeDiscovery().discover();
		  LOGGER.info("Found LibVLC {}", found);
      LOGGER.info("Version {}", LibVlc.INSTANCE.libvlc_get_version());

      System.setProperty("jna.dump_memory", DUMP_NATIVE_MEMORY);
      //Canvas vs = new Canvas();
      //vs.setBackground(Color.black);

      factory = new MediaPlayerFactory();

      //mediaPlayer = factory.newHeadlessMediaPlayer();
      //mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));
      Canvas vs = new Canvas();
      mediaPlayer = factory.newEmbeddedMediaPlayer();
      //mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));
      mediaPlayer.setPlaySubItems(true); // <--- This is very important for YouTube media

      mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
          @Override
          public void buffering(MediaPlayer mediaPlayer, float newCache) {
              LOGGER.info("Buffering " + newCache);
          }

          @Override
          public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
              List<String> items = mediaPlayer.subItems();
              LOGGER.info("items", items);
          }
      });
    }

    public void Play() {
        mediaPlayer.playMedia("https://www.youtube.com/watch?v=6g0-pDRmtHs");
    }
}