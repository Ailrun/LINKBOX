package org.sopt.linkbox.custom.helper.tagHelper;

import java.io.Serializable;

/**
 * Created by MinGu on 2015-08-30.
 */
public class IndividualTag implements Serializable {
    private String tagName;

    public IndividualTag(String tag){ tagName = tag; }
    public String getTagName(){ return tagName; }

    @Override
    public String toString(){ return tagName; }
}

