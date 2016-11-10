import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;


public class Test {
	public static final Set<Character> urlBreaker = new HashSet<Character>();
	static {
		urlBreaker.add('(');
		urlBreaker.add(')');
		urlBreaker.add(',');
		urlBreaker.add('\"');
		urlBreaker.add('\'');
		urlBreaker.add(';');
		urlBreaker.add('>');
		urlBreaker.add('<');
		urlBreaker.add('{');
		urlBreaker.add('}');
		urlBreaker.add('[');
		urlBreaker.add(']');

		// local
		urlBreaker.add('¡£');
		urlBreaker.add('£º');
		urlBreaker.add('£¨');
		urlBreaker.add('£©');
		urlBreaker.add('£¬');
		urlBreaker.add('¡±');
		urlBreaker.add('¡®');
		urlBreaker.add('£»');
		urlBreaker.add('¡¶');
		urlBreaker.add('¡·');

		// space
		urlBreaker.add('\n');
		urlBreaker.add((char) 32);
		urlBreaker.add('\r');
		urlBreaker.add('\t');
		urlBreaker.add('\\');
		urlBreaker.add('\b');
	}

	public static void main(String[] args) throws Exception {
		// FileOutputStream fos = new FileOutputStream(file1);
		// fos.write(new byte[]{1,2,3,4,5,6,7,8,9,0});
		// fos.flush();fos.close();

		// File file2=new File(new
		// File(Test.class.getClassLoader().getResource("aaaa").toURI()).getPath()+"cc");
		// File file3=file2.getParentFile();
		// System.out.println(file2.exists()+",read="+file2.canRead()+",exe="+file2.canExecute()+",write="+file2.canWrite());
		// System.out.println(file3.exists()+",read="+file3.canRead()+",exe="+file3.canExecute()+",write="+file3.canWrite());

		// FileOutputStream fos = new FileOutputStream(file1.getPath());
		// FileInputStream fis = new FileInputStream(file1);
		// byte[] buf = new byte[fis.available()];
		// fis.read(buf);
		// System.out.println(new String(buf));
		// fos.write(buf);fos.flush();fis.close();fos.close();
		System.out.println(indexOfUrlBreakSymbo("ef fewf >fe¡¶¡¶ef", 12));
		
	}

	
	public static int indexOfUrlBreakSymbo(String content,int startOffset){
		return indexOfBreakSymbol(content, startOffset, urlBreaker);
	}
	
	public static int indexOfBreakSymbol(String content,int startOffset,Set<Character> bs){
		int length=content.length();
		if(startOffset>length)
			startOffset=length;
		if(startOffset<0) startOffset=0;
		
		for(int i=startOffset;i<length;i++){
			if(bs.contains(content.charAt(i))){
				return i;
			}
		}
		
		return -1;
		
	}
	
	
	public static class FileSaver implements Runnable {
		// TODO
		public File src;
		public long startOffset;
		public int modifiedLength, bufsize = 1024;
		public byte[] newContent;
		public byte[] buf = new byte[bufsize];
		public boolean stop;

		public long doneBytes = 0;

		public FileSaver(File src, long startOffset, int modifiedLength,
				byte[] newContent) {
			this.src = src;
			this.startOffset = startOffset;
			this.modifiedLength = modifiedLength;
			this.newContent = newContent;
			if (this.startOffset < 0)
				this.startOffset = 0;
			if (this.modifiedLength < 0)
				this.modifiedLength = 0;
			if (this.newContent == null)
				this.newContent = new byte[0];
			stop = false;
		}

		public void run() {
			if (src == null || !src.exists() || src.isDirectory())
				return;
			if (this.startOffset > src.length())
				this.startOffset = src.length();
			RandomAccessFile raf = null;
			try {
				raf = new RandomAccessFile(src, "rw");
				if (modifiedLength > newContent.length) {
					shortenSave(raf);
				} else if (modifiedLength == newContent.length) {
					fixedSave(raf);
				} else {
					extendedSave(raf);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (raf != null)
					try {
						raf.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}

		private void shortenSave(RandomAccessFile raf) throws IOException {
			raf.seek(startOffset);
			raf.write(newContent);
			long writeIndex, readIndex;
			int count = 0;
			readIndex = startOffset + modifiedLength;
			writeIndex = startOffset + newContent.length;
			raf.seek(readIndex);
			while (!stop && (count = raf.read(buf)) != -1) {
				raf.seek(writeIndex);
				raf.write(buf, 0, count);
				readIndex += count;
				writeIndex += count;
				raf.seek(readIndex);
			}
			System.out.println(count + "   @FileUtils 770line");
			raf.setLength(writeIndex);

			// TODO
			buf = new byte[1024];
			raf.seek(0);
			count = raf.read(buf);
			System.out.println(count + ":" + new String(buf, 0, count));
		}

		private void extendedSave(RandomAccessFile raf) throws IOException {
			long oldlength = raf.length();
			long rest = oldlength - startOffset - modifiedLength;
			long newlength = rest > 0 ? oldlength + newContent.length
					- modifiedLength : startOffset + newContent.length;
			System.out.println(newlength);
			raf.setLength(newlength);
			long writeIndex, readIndex;
			int count = getExtendedReadCount(oldlength);
			System.out.println(count);
			readIndex = oldlength - count;
			writeIndex = newlength - count;
			while (!stop && count > 0) {
				raf.seek(readIndex);
				raf.read(buf, 0, count);
				System.out.println(new String(buf, 0, count));
				raf.seek(writeIndex);
				raf.write(buf, 0, count);
				count = getExtendedReadCount(readIndex);
				readIndex -= count;
				writeIndex -= count;
			}
			raf.seek(startOffset);
			raf.write(newContent);

			// TODO
			raf.seek(0);
			buf = new byte[1024];
			count = raf.read(buf);
			System.out.println(count + ":" + new String(buf, 0, count));
		}

		private int getExtendedReadCount(long readIndex) {
			long rest = readIndex - startOffset - modifiedLength;
			return (int) (rest > bufsize ? bufsize : rest);
		}

		private void fixedSave(RandomAccessFile raf) throws IOException {
			raf.seek(startOffset);
			raf.write(newContent);
			System.out.println("fixed");
		}

		public void stop() {
			stop = true;
		}
	}

	public static class FileSaveaser implements Runnable {
		// TODO
		public File src, dest;
		private long startOffset;
		private int modifiedLength, bufsize = 50;
		private byte[] newContent;
		private byte[] buf = new byte[bufsize];
		private boolean stop;

		public long doneBytes = 0;

		public FileSaveaser(File src, File dest, long startOffset,
				int modifiedLength, byte[] newContent) {
			this.src = src;
			this.dest = dest;
			this.startOffset = startOffset;
			this.modifiedLength = modifiedLength;
			this.newContent = newContent;
			if (this.startOffset < 0)
				this.startOffset = 0;
			if (this.modifiedLength < 0)
				this.modifiedLength = 0;
			if (this.newContent == null)
				this.newContent = new byte[0];
			stop = false;
		}

		public void run() {
			if (src == null || !src.exists() || src.isDirectory()
					|| dest == null)
				return;
			if (this.startOffset > src.length())
				this.startOffset = src.length();
			FileInputStream fis = null;
			FileOutputStream fos = null;
			try {
				fis = new FileInputStream(src);
				fos = new FileOutputStream(dest);
				saveas(fis, fos);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (fis != null)
						fis.close();
					if (fos != null)
						fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void saveas(FileInputStream fis, FileOutputStream fos)
				throws IOException {
			int count = 0;
			doneBytes = 0;
			while (!stop && startOffset > doneBytes) {
				count = fis.read(buf, 0, getReadCount());
				fos.write(buf, 0, count);
				fos.flush();
				doneBytes += count;
				System.out.println(doneBytes + ",count=" + count + "  @phase1");
			}
			if (!stop) {
				fos.write(newContent);
				fos.flush();
				doneBytes += newContent.length;
				count = (int) fis.skip(modifiedLength);
				System.out.println(doneBytes + ",count=" + count + "  @phase2");
			}
			while (!stop && (count = fis.read(buf)) != -1) {
				fos.write(buf, 0, count);
				fos.flush();
				doneBytes += count;
				System.out.println(doneBytes + ",count=" + count + "  @phase3");
			}
			buf = new byte[1024];
			FileInputStream fis2 = new FileInputStream(src);
			count = fis2.read(buf);
			System.out.println(new String(buf, 0, count) + "   @over");
			FileInputStream is = new FileInputStream(dest);
			count = is.read(buf);
			System.out.println(new String(buf, 0, count) + "  @over");

		}

		private int getReadCount() {
			return (int) (startOffset - doneBytes <= 0 ? bufsize : startOffset
					- doneBytes >= bufsize ? bufsize : startOffset - doneBytes);
		}

		public void stop() {
			stop = true;
		}
	}

}
