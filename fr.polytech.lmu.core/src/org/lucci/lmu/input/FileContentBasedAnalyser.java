package org.lucci.lmu.input;

import org.lucci.lmu.Model;

public interface FileContentBasedAnalyser {
	Model analyse(byte[] data) throws Exception;
}
