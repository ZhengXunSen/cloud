package com.zxs.cloud.util;

/**
 * ORCUtil.java
 * Copyright
 * @author
 */
/**
 * ORC工具类
 *
 * @author
 * @Date
 */
public class ORCUtil{

    /*private Text t = new Text();
    private StructObjectInspector soi = null;
    private OrcStruct orc = null;
    private ObjectInspector oi = null;
    private List<Object> list = null;
    private OrcSerde serde = null;
    *//**
     * 根据hive表描述设置读取ORC文件时使用的typeInfo
     *
     * @param type 		ORC格式的hive表描述
     *//*
    public void setORCtype(String type) {
        TypeInfo typeInfo = TypeInfoUtils.getTypeInfoFromTypeString(type);
        soi = (StructObjectInspector) OrcStruct.createObjectInspector(typeInfo);
    }
    *//**
     * ORC格式文件中的数据，相当于hive中的一行记录，是由orcNewInputFormat读取的
     *
     * @param orcStruct		hive中的一行记录
     *//*
    public void setRecord(OrcStruct orcStruct) {
        this.orc = orcStruct;
    }
    *//**
     * 拿取orc格式文件一行记录中的某个字段的值
     *
     * @param key		hive表中的某个字段
     * @return			字段对应的值
     *//*
    public String getData(String key) {
        StructField structFieldRef = soi.getStructFieldRef(key);
        String values = String.valueOf(soi.getStructFieldData(this.orc, structFieldRef));
        return EmptyUtil.isEmpty(values) || values.toLowerCase().equals("null") ? null : values;
    }
    *//**
     * 初始化ORC配置用于写出ORC格式文件
     *//*
    public void setORCWriteType(String type){
        TypeInfo typeInfo = TypeInfoUtils.getTypeInfoFromTypeString(type);
        oi = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(typeInfo);
    }
    *//**
     * 添加用于输出的字段
     *
     * @param objs		按ORC配置排序好的数据，一个一个的添加
     *//*
    public ORCUtil addAttr(Object... objs){
        if(EmptyUtil.isEmpty(list)){
            list = new ArrayList<Object>();
        }
        for(Object o:objs){
            list.add(o);
        }
        return this;
    }
    *//**
     * 将添加的输出字段转换成用于MR输出的ORC序列化对象
     * 注意：每输出一次就要清空一次缓存的数据
     *
     * @return 			ORC数据的序列化对象
     *//*
    public Writable serialize(){
        if(EmptyUtil.isEmpty(serde)){
            serde = new OrcSerde();
        }
        serde=new OrcSerde();
        Writable line = serde.serialize(list, oi);
        list = new ArrayList<Object>();
        return line;
    }
    public void serialize(TaskInputOutputContext context) throws IOException, InterruptedException{
        if(EmptyUtil.isEmpty(serde)){
            serde = new OrcSerde();
        }
        Writable line = serde.serialize(list, oi);
        list = new ArrayList<Object>();
        context.write(NullWritable.get(), line);
    }*/
}

