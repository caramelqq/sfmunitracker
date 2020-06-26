package com.davidqq.sfmunitracker.remoteapi.xmlelements;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Xml;

@Xml(name="lastTime")
public class LastTime {
    @Attribute(name="time")
    public String time;
}