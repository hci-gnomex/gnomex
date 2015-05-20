package hci.gnomex.utility;


import hci.gnomex.model.Topic;

import java.io.Serializable;
import java.util.Comparator;

public class TopicComparator implements Comparator<Topic>, Serializable {
	public int compare(Topic t1, Topic t2) {
		return t1.getIdTopic().compareTo(t2.getIdTopic());
	}
}
