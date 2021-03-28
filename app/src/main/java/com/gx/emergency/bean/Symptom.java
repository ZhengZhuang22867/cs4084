package com.gx.emergency.bean;

import org.json.JSONArray;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 症状实体类
 */
public class Symptom extends DataSupport {
    private String name;//症状名称
    private String symptom;//症状
    private String tools;//工具集合
    private String notice;//注意事项
    private String forbid;//禁止事项
    private String steps;//步骤

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getForbid() {
        return forbid;
    }

    public void setForbid(String forbid) {
        this.forbid = forbid;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Symptom(String name,String symptom,String tools, String notice, String forbid, String steps) {
        this.name = name;
        this.symptom = symptom;
        this.tools=tools ;
        this.notice = notice;
        this.forbid = forbid;
        this.steps = steps;
    }
}
