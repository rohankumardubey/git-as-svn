/*
 * This file is part of git-as-svn. It is subject to the license terms
 * in the LICENSE file found in the top-level directory of this distribution
 * and at http://www.gnu.org/licenses/gpl-2.0.html. No part of git-as-svn,
 * including this file, may be copied, modified, propagated, or distributed
 * except according to the terms contained in the LICENSE file.
 */
package svnserver;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Version information.
 *
 * @author Artem V. Navrotskiy <bozaro@users.noreply.github.com>
 */
public final class VersionInfo {
  private static VersionInfo s_instance = new VersionInfo();

  @Nullable
  private final String revision;
  @Nullable
  private final String tag;

  private VersionInfo() {
    try (InputStream stream = getClass().getResourceAsStream("VersionInfo.properties")) {
      if (stream == null) {
        throw new IllegalStateException();
      }
      final Properties props = new Properties();
      props.load(stream);
      revision = getProperty(props, "revision", null);
      tag = getProperty(props, "tag", null);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @NotNull
  public static String getVersionInfo() {
    if (s_instance.revision == null)
      return "none version info";

    if (s_instance.tag == null)
      return s_instance.revision;

    return s_instance.tag + ", " + s_instance.revision;
  }

  @Contract("_, _, null -> _; _, _, !null -> !null")
  private static String getProperty(@NotNull Properties props, @NotNull String name, @Nullable String defaultValue) {
    final String value = props.getProperty(name);
    return value != null && !value.startsWith("${") ? value : defaultValue;
  }
}
