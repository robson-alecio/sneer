// Prevayler, The Free-Software Prevalence Layer
// Copyright 2001-2006 by Klaus Wuestefeld
//
// This library is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.
//
// Prevayler is a trademark of Klaus Wuestefeld.
// See the LICENSE file for license details.

package sneer.pulp.serialization.mocks;

import java.io.InputStream;
import java.io.OutputStream;

import wheel.io.serialization.Serializer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.binary.BinaryStreamReader;
import com.thoughtworks.xstream.io.binary.BinaryStreamWriter;

public class XStreamBinarySerializer implements Serializer {

    private ThreadLocal<XStream> _xstreams = new ThreadLocal<XStream>() {
        @Override protected XStream initialValue() {
            return createXStream();
        }
    };

    protected XStream getXStream() {
        return _xstreams.get();
    }

    @Override
    public void writeObject(OutputStream stream, Object object) {
    	BinaryStreamWriter writer = new BinaryStreamWriter(stream);
    	getXStream().marshal(object, writer);
        writer.flush();
    }

    @Override
    public Object readObject(InputStream stream) {
    	return getXStream().unmarshal(new BinaryStreamReader(stream));
    }

    /**
     * Create a new XStream instance. This must be a new instance because
     * XStream instances are not threadsafe.
     */
    protected XStream createXStream() {
        return new XStream();
    }

}
