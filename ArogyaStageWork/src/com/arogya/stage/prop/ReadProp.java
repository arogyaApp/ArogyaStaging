package com.arogya.stage.prop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class ReadProp {

	public static Properties q = new Properties();
	public static InputStream in = null;
	public static HashMap<String, String> map = new HashMap<String, String>();

	public ReadProp() {

		try {
			in = new FileInputStream("dataconfig.properties");

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		try {
			q.load(in);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static HashMap<String, String> LoadPropintoHashMap() {

		for (final Entry<Object, Object> entry : q.entrySet()) {
			map.put((String) entry.getKey(), (String) entry.getValue());
		}

		return map;

	}

}
