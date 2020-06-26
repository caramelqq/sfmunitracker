package com.davidqq.sfmunitracker.remoteapi.xmlelements;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml(name="direction")
public class Direction {
    @Attribute(name="tag")
    public String tag;

    @Attribute(name="title")
    public String title;

    @Attribute(name="name")
    public String name;

    @Attribute(name="useForUI")
    public String useForUI;

    @Element
    public List<Stop> stop;
}