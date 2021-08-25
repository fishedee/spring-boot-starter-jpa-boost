package com.fishedee.jpa_boost.lint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class Processor {
    //全局数据
    private static ProcessorConfig config;

    private static List<JPALinter> extraLinters;

    public static void setConfig(BeanFactory beanFactory,ProcessorConfig config){
        Processor.config = config;

        Class<? extends JPALinter>[] extendProcessors = config.getExtraLinters();
        Processor.extraLinters = new ArrayList<>();
        if( extendProcessors!= null && extendProcessors.length != 0 ){
            for( int i = 0 ;i != extendProcessors.length ;i++){
                try {
                    JPALinter processor = beanFactory.getBean(extendProcessors[i]);
                    Processor.extraLinters.add(processor);
                }catch(Exception e ){
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private JPAWrapper bean;

    private Assert assertUtil;

    public Processor(JPAWrapper bean){
        this.bean = bean;
        this.assertUtil = new Assert(bean.getClassName());
    }

    public void process(){
        if( bean.hasEntityAnnotation() ){
            entityProcess();
        }else if( bean.hasEmbeddableAnnotation()){
            embeddableProcess();
        }
        //额外的处理器
        for(int i = 0; i != Processor.extraLinters.size(); i++){
            Processor.extraLinters.get(i).process(bean);
        }
    }

    public void entityProcess(){
        if( config.getSuperEntityClass() != null ){
            this.bean.isDirectExtendClass(config.getSuperEntityClass());
        }

        this.shouldHaveIdProperty();

        this.bean.checkPublicAccess();
        this.processField();
        this.processMethod();
        this.processConstructor();
    }

    public void embeddableProcess(){
        if( config.getSuperEmbeddableClass() != null ){
            this.bean.isDirectExtendClass(config.getSuperEmbeddableClass());
        }

        this.shouldHavenotIdProperty();

        this.bean.checkPublicAccess();
        this.processField();
        this.processMethod();
        this.processConstructor();
    }

    private void shouldHaveIdProperty(){
        Map<String,FieldWrapper> fieldMap = this.bean.getFields();
        FieldWrapper idField = fieldMap.get("id");
        this.assertUtil.assertNotNull(" should have id property ",idField);
        idField.shouldHaveIdAnnotation();
    }

    private void shouldHavenotIdProperty(){
        Map<String,FieldWrapper> fieldMap = this.bean.getFields();
        FieldWrapper idField = fieldMap.get("id");
        this.assertUtil.assertNull(" should have not id property ",idField);
    }
    private void processField(){
        Map<String,FieldWrapper> fieldMap = this.bean.getFields();

        //检查其他属性
        for( FieldWrapper field : fieldMap.values()){
            if( config.isAllowIdHaveGeneratedValue() == false ){
                field.shouldNotHaveGeneratedValue();
            }
            field.shouldNotSynthetic();
            field.shouldPrivateAccess();
            if( field.isIdName() ){
                continue;
            }
            //检查属性
            if( field.isListType()){
                //list属性
                processListField(field);
            }else if( field.isSetType() ){
                //set属性
                processSetField(field);
            }else if( field.isMapType() ){
                //map属性
                processMapField(field);
            }else if( field.isEnumType()){
                //enum属性
                processEnumField(field);
            }else if( field.hasJsonUnwrappedAnnotation()){
                //embeddable属性
                processEmbeddableField(field);
            }else{
                //基础属性
                if( field.hasTransitAnnotation() ){
                    //略过属性
                    field.shouldHaveJsonIgnoreAnnotation();
                    field.shouldHaveAutowiredAnnotation();
                }else{
                    //非略过属性
                    field.shouldSatifyDbType();
                }
            }
        }
    }

    private  void processListField(FieldWrapper field){
        //检查字段自身的注解
        if( field.hasOneToManyAnnotation()){
            //嵌套实体
            field.checkOneToManyAnnotation();
            field.checkFetchAnnotation();
            field.checkJoinColumnAnnotation();
            field.shouldHaveOrderColumnAnnotation();
        }else{
            //嵌套Embeddable
            field.checkElementCollectionAnnotation();
            field.checkFetchAnnotation();
            field.shouldHaveOrderColumnAnnotation();
        }

        //检查嵌套的元素类型
        Class elemType = field.getListElemType();
        if( TypeUtil.isSatifyDbType(elemType) == false){
            JPAWrapper elemWrapper = LintProcessor.process(elemType);
            //非基础类型
            if( field.hasOneToManyAnnotation()){
                elemWrapper.shouldHaveEntityAnnotation();
            }else{
                elemWrapper.shouldHaveEmbeddableAnnotation();
            }
        }

    }

    private  void processSetField(FieldWrapper field){
        //检查字段自身的注解
        if( field.hasOneToManyAnnotation()){
            //嵌套实体
            field.checkOneToManyAnnotation();
            field.checkFetchAnnotation();
            field.shouldHaveColumnAnnotation();
        }else{
            //嵌套Embeddable
            field.checkElementCollectionAnnotation();
            field.checkFetchAnnotation();
            field.shouldHaveColumnAnnotation();
        }

        //检查嵌套的元素类型
        Class elemType = field.getSetElemType();
        this.assertUtil.assertTrue(field.getName()+" should satify db type",TypeUtil.isSatifyDbType(elemType));
    }

    private  void processMapField(FieldWrapper field){
        //检查字段自身的注解
        if( field.hasOneToManyAnnotation()){
            //嵌套实体
            field.checkOneToManyAnnotation();
            field.checkFetchAnnotation();
            field.shouldHaveMapKeyColumnAnnotation();
        }else{
            //嵌套Embeddable
            field.checkElementCollectionAnnotation();
            field.checkFetchAnnotation();
            field.shouldHaveMapKeyColumnAnnotation();
        }

        //检查嵌套的元素类型
        Class[] elemTypes = field.getMapElemType();
        Class keyType = elemTypes[0];
        Class valueType = elemTypes[1];
        this.assertUtil.assertTrue(field.getName()+" should satify db type",TypeUtil.isSatifyDbType(keyType));

        if( TypeUtil.isSatifyDbType(valueType) == false){
            JPAWrapper elemWrapper = LintProcessor.process(valueType);
            //非基础类型
            if( field.hasOneToManyAnnotation()){
                elemWrapper.shouldHaveEntityAnnotation();
            }else{
                elemWrapper.shouldHaveEmbeddableAnnotation();
            }
        }
    }

    private void processEnumField(FieldWrapper field){
        field.checkEnumTypeAnnotation();
    }

    private void processEmbeddableField(FieldWrapper field){
        JPAWrapper elemWrapper = LintProcessor.process(field.getType());
        elemWrapper.shouldHaveEmbeddableAnnotation();
    }

    private void processConstructor(){
        //protected的构造函数
        List<ConstructorWrapper> constructors = this.bean.getConstructor();

        ConstructorWrapper defaultConstructor = null;
        for( ConstructorWrapper constructor : constructors){
            if( constructor.isEmptyParameter() == true){
                defaultConstructor = constructor;
                break;
            }
        }

        this.assertUtil.assertNotNull("Should have empty argument constructor",defaultConstructor);
        defaultConstructor.checkPublicOrProtectedAccess();
    }

    private void processMethod(){
        Map<String,MethodWrapper> methodMap = this.bean.getMethods();

        //toString方法
        MethodWrapper methodWrapper = methodMap.get("toString");
        this.assertUtil.assertNotNull("shouldHaveToStringMethod",methodWrapper);
        methodWrapper.checkToStringName();

        //setId方法
        MethodWrapper methodWrapper2 = methodMap.get("setId");
        this.assertUtil.assertNull("shouldNotHave setId method or @Setter annotation or @Data annotation",methodWrapper2);
    }
}
