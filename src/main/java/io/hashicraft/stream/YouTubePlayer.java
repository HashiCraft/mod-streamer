package io.hashicraft.stream;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.media.Media;
import uk.co.caprica.vlcj.media.MediaEventAdapter;
import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
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
        NativeLibrary.addSearchPath(uk.co.caprica.vlcj.binding.RuntimeUtil.getLibVlcLibraryName(),
            NATIVE_LIBRARY_SEARCH_PATH);
      }

      boolean found = new NativeDiscovery().discover();
		  LOGGER.info("Found LibVLC {}", found);
      LOGGER.info("Version {}", LibVlc.libvlc_get_version());

      System.setProperty("jna.dump_memory", DUMP_NATIVE_MEMORY);
      //Canvas vs = new Canvas();
      //vs.setBackground(Color.black);

      factory = new MediaPlayerFactory();

      //mediaPlayer = factory.newHeadlessMediaPlayer();
      //mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));
      Canvas vs = new Canvas();
      mediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
      //mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));
      //mediaPlayer.setPlaySubItems(true); // <--- This is very important for YouTube media

      /*
      mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
          @Override
          public void buffering(MediaPlayer mediaPlayer, float newCache) {
              LOGGER.info("Buffering " + newCache);
          }

          @Override
          public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
              String items = mediaPlayer.media().subitems().toString();
              LOGGER.info("items", items);
          }
      });
      */
    }

    public void Play() {
        mediaPlayer.media().prepare("https://www.youtube.com/watch?v=6g0-pDRmtHs");
        mediaPlayer.controls().play();
    }
}