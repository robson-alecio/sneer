package spikes.wheel.io;

public class JarExploder {
	
//	private final File _target;
//	private final JarFile _jarFile;
//	private final Predicate<JarEntry> _skipPredicate;
//
//	public JarExploder(JarFile jarFile, File target, Predicate<JarEntry> skipPredicate) {
//		_jarFile = jarFile;
//		_target = target;
//		_skipPredicate = skipPredicate;
//	}
//
//	public JarFile jarFile() {
//		return _jarFile;
//	}
//
//	public void explode() throws IOException {
//		Enumeration<JarEntry> e = jarFile().entries();
//		while (e.hasMoreElements()) {
//			JarEntry entry = e.nextElement();
//			if (_skipPredicate.evaluate(entry))
//				continue;
//			explodeEntry(entry);
//		}
//	}
//
//	private void explodeEntry(JarEntry entry) throws IOException,
//			FileNotFoundException {
//		if (entry.isDirectory()) {
//			explodeDirectory(entry.getName());
//		} else {
//			explodeFile(entry);
//		}
//	}
//
//	private void explodeFile(JarEntry entry) throws IOException,
//			FileNotFoundException {
//		final File file = new File(_target, entry.getName());
//		FileUtils.touch(file);
//		explodeEntryTo(entry, file);
//	}
//
//	private void explodeDirectory(String name) {
//		File dir = new File(_target, name);
//		dir.mkdirs();
//	}
//
//	private void explodeEntryTo(JarEntry entry, File file) throws IOException,
//			FileNotFoundException {
//		InputStream is = inputStreamFor(entry);
//		try {
//			final FileOutputStream os = new FileOutputStream(file);
//			try {
//				IOUtils.copy(is, os);
//			} finally {
//				IOUtils.closeQuietly(os);
//			}
//		} finally {
//			IOUtils.closeQuietly(is);
//		}
//	}
//	
//	private InputStream inputStreamFor(final ZipEntry entry) throws IOException {
//		return jarFile().getInputStream(entry);
//	}

}
