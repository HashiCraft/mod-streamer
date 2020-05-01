package io.hashicraft.stream;

import uk.co.caprica.vlcj.medialist.MediaList;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.util.List;

import com.sun.jna.NativeLibrary;


import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.binding.LibC;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;
/**
 * A minimal YouTube player.
 * <p>
 * The URL/MRL must be in the following format:
 *
 * <pre>
 *   http://www.youtube.com/watch?v=000000
 * </pre>
 *
 * The only thing that makes this different from a 'regular' media player application is the
 * following piece of code:
 *
 * <pre>
 * mediaPlayer.setPlaySubItems(true); // &lt;--- This is very important for YouTube media
 * </pre>
 *
 * Note that it is also possible to programmatically play the sub-item in response to events - this
 * is slightly more complex but more flexible.
 * <p>
 * The YouTube web page format changes from time to time. This means that the lua scripts that vlc
 * provides to parse the YouTube web pages when looking for the media to stream may not work. If you
 * get errors, especially errors alluding to malformed urls, then you may need to update your vlc
 * version to get newer lua scripts.
 */
public class YouTubePlayer {

  private final MediaPlayerFactory factory;
  private final EmbeddedMediaPlayer mediaPlayer;

  private static final String NATIVE_LIBRARY_SEARCH_PATH = null;
  private static final String DUMP_NATIVE_MEMORY = "false";

  static {

        if(null != NATIVE_LIBRARY_SEARCH_PATH) {
            //logger.info("Explicitly adding JNA native library search path: '{}'", NATIVE_LIBRARY_SEARCH_PATH);
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        }

        System.setProperty("jna.dump_memory", DUMP_NATIVE_MEMORY);
    }

    public YouTubePlayer() {
        //Canvas vs = new Canvas();
        //vs.setBackground(Color.black);

        factory = new MediaPlayerFactory();

        mediaPlayer = factory.newEmbeddedMediaPlayer();
        //mediaPlayer.setVideoSurface(factory.newVideoSurface(vs));

        mediaPlayer.setPlaySubItems(true); // <--- This is very important for YouTube media

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void buffering(MediaPlayer mediaPlayer, float newCache) {
                System.out.println("Buffering " + newCache);
            }

            @Override
            public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem) {
                List<String> items = mediaPlayer.subItems();
                System.out.println(items);
            }
        });
    }

    public void Play() {
        mediaPlayer.playMedia("https://www.youtube.com/watch?v=6g0-pDRmtHs");
    }
}