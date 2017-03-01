package hci.gnomex.utility;

import hci.gnomex.constants.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

	/*
	 * Indicates if file is a link file on unix.
	 */
	public static boolean isSymlink(File file) {
		try {
			if (file == null) {
				return false;
			}
			File canon;
			if (file.getParent() == null) {
				canon = file;
			} else {
				File canonDir = file.getParentFile().getCanonicalFile();
				canon = new File(canonDir, file.getName());
			}

			return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
		} catch (IOException ex) {
			return false;
		}
	}

	public static boolean renameTo (File sourceFile, File destFile) {
		boolean success = false;
		try {
			Path sourcePath = sourceFile.toPath();
			Path targetPath = destFile.toPath();
			Files.move(sourcePath,targetPath);
			success = true;
		}
		catch (Exception rex) {
			System.out.println ("[FileUtil.renameTo] move error: " + rex.toString());
			success = false;
		}

		return success;
	}

	/**
	 * Recursively finds and removes any empty subdirectories in the provided directory.
	 * Removes the provided directory if it is (or becomes) empty.
	 * @param directoryName The path of the directory to be pruned
     */
	public static void pruneEmptyDirectories(String directoryName) {
		File directory = new File(directoryName);
		String directoryPath = directory.getAbsolutePath().replace("\\", Constants.FILE_SEPARATOR);
		if (directory.exists()) {
			for (String fileName : directory.list()) {
				File file = new File(directoryPath + Constants.FILE_SEPARATOR + fileName);
				if (file.isDirectory()) {
					pruneEmptyDirectories(file.getAbsolutePath());
				}
			}
			if (directory.list().length == 0) {
				directory.delete();
			}

		}

	}

}
