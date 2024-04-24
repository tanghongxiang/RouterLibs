package com.thx.logicprocessormodule;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.Type.ClassType;
import com.thx.logicroutermodule.AbstractLogic;
import com.thx.logicroutermodule.annotation.Logic;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
@AutoService(Processor.class)
public class LogicProcessor extends AbstractProcessor {
    /* ======================================================= */
    /* Fields                                                  */
    /* ======================================================= */

    private Filer mFiler;

    private static final String BASE_LOGIC_FULL_NAME = AbstractLogic.class.getCanonicalName();

    /**
     * 已经处理过的数据
     */
    private Set<Element> mHandledElement = new HashSet<>();

    /**
     * 生成的类文件所在 package
     */
    private String mDestPackage = "com.thx.perfect.logic";

    /**
     * 生成的类文件的文件名
     */
    private String mDestClass = "AppAptLogicMap";



    /* ======================================================= */
    /* Override/Implements Methods                             */
    /* ======================================================= */

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();

        String tempPackage = processingEnv.getOptions().get("logicRouterDstPkg");
        if (tempPackage != null) {
            mDestPackage = tempPackage;
        }

        String tempClsName = processingEnv.getOptions().get("logicRouterClsName");
        if (tempClsName != null) {
            mDestClass = tempClsName;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // 获取所有被 @Logic 注解的类
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Logic.class);

        // 创建一个函数, 返回值类型： Map<String, Class<? extends AbstractLogic>>
        ParameterizedTypeName inputMapTypeOfRoot = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(
                        ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(AbstractLogic.class))
                )
        );

        // 函数名、Modifiers、函数Body
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(inputMapTypeOfRoot)
                .addStatement("$T<$T, $T<? extends $T>> map = new $T<>(" + elements.size() + ")", Map.class, String.class, Class.class, AbstractLogic.class, HashMap.class)
                .addJavadoc("这个方法是自动生成的。\n");

        boolean fileChanged = false;

        // 遍历每一个类
        for (Element element : elements) {
            if (mHandledElement.contains(element)) {
                continue;
            }

            // 获取其类型
            TypeMirror type = element.asType();

            // 判断被注解的对象是不是一个类
            if (!(type instanceof ClassType) || element.getKind() != ElementKind.CLASS) {
                // @Logic 注解只能用在类上
                throw new IllegalArgumentException("@Logic 注解只能用到类上。目前用到了 [" + type.toString() + "] 上");
            }

            // 判断被注解的对象是不是 AbstractLogic 的子类。
            ClassType classType = (ClassType) type;
            boolean isAbstractLogic = isSubClassOf(classType, BASE_LOGIC_FULL_NAME);
            if (!isAbstractLogic) {
                // @Logic 注解只能用在 AbstractLogic 的子类上
                throw new IllegalArgumentException("@Logic 注解只能用在 AbstractLogic 的子类上。目前用到了 [" + type.toString() + "] 上");
            }

            // 获取注解包含的参数值
            Logic annotation = element.getAnnotation(Logic.class);
            String name = annotation.value();

            // 在函数中添加一行
            methodBuilder.addStatement("map.put(\"" + name + "\", $T.class)", classType);

            // 添加到已经处理过的数据集
            mHandledElement.add(element);
            fileChanged = true;
        }

        if (!fileChanged) {
            return true;
        }

        // 生成函数
        MethodSpec method = methodBuilder.addStatement("return map").build();
        // 创建一个类
        TypeSpec aptLogicMap = TypeSpec.classBuilder(mDestClass)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(method)
                .addJavadoc("这个类是由{@link " + getClass().getCanonicalName() + "} 自动生成的。\n"
                        + "会把所有添加了 @Logic 注解的 AbstractLogic 的子类都注册到 {@link #map() } 中。\n\n"
                        + "@author thx\n"
                        + "@since 2021/11/11 10:36 AM \n")
                .build();

        // 创建一个 package
        JavaFile javaFile = JavaFile.builder(mDestPackage, aptLogicMap)
                .addFileComment("这是自动生成的代码")
                .build();

        try {
            javaFile.writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Logic.class.getCanonicalName());
        return annotations;
    }


    @SuppressWarnings("SameParameterValue")
    private boolean isSubClassOf(ClassType type, String parentClassName) {
        ClassType current = type;
        while (true) {
            Type superType = current.supertype_field;
            if (superType == null && current.tsym == null) {
                return false;
            }

            if (superType == null) {
                superType = ((ClassType) (current.tsym.type)).supertype_field;
            }

            if (superType.tsym.toString().equals(parentClassName)) {
                return true;
            }
            if (!(superType instanceof ClassType)) {
                return false;
            }
            current = (ClassType) superType;
        }
    }
}