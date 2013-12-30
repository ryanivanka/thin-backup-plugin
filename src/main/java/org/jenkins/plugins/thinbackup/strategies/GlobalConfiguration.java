package org.jenkins.plugins.thinbackup.strategies;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.jenkins.plugins.thinbackup.exceptions.RestoreException;

final class GlobalConfiguration extends AbstractStrategy implements IStrategy {

  public GlobalConfiguration(File jenkinsHome) {
    super(jenkinsHome);
  }

  @Override
  public Collection<File> backup() {
    // formatter:off
    IOFileFilter suffixFilter = FileFilterUtils.or(
        FileFilterUtils.suffixFileFilter(".xml"), 
        FileFilterUtils.suffixFileFilter(".key"));
    // formatter:on
    FileFilter filter = FileFilterUtils.and(FileFileFilter.FILE, suffixFilter);
    
    return Arrays.asList(getJenkinsHome().listFiles(filter));
  }

  @Override
  public void restore(List<File> toRestore) {
    for (File file : toRestore) {
      try {
        if (file.isFile())
          FileUtils.copyFileToDirectory(file, getJenkinsHome());
      } catch (IOException e) {
        new RestoreException("Cannot restore global configuration file.", e);
      }
    }
  }

}