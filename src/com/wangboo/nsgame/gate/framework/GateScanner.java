package com.wangboo.nsgame.gate.framework;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.wangboo.nsgame.gate.framework.annotation.Gate;
import com.wangboo.nsgame.gate.framework.annotation.Param;
import com.wangboo.nsgame.gate.framework.define.GateDefine;
import com.wangboo.nsgame.gate.framework.define.ParamDefine;

/**
 * 网关定义
 * @author wangboo
 *
 */
public class GateScanner {

	private static String binPath;
	
	private String scanPkg;
	
	private Pattern classnamePattern;
	
	private Class<?> messageClass;
	
	private Class<?> messageIdClass;
	
	private ApplicationContext context;
	
	public GateScanner(ApplicationContext context, Class<?> messageIdClass, Class<?> messageClass) {
		this.context = context;
		classnamePattern = Pattern.compile("(\\w+)\\.class$");
		this.messageClass = messageClass;
		this.messageIdClass = messageIdClass;
	}
	
	public static void setBinpath(String binPath) {
		if(binPath.endsWith("/")) {
			GateScanner.binPath = binPath;
		}else {
			GateScanner.binPath = binPath + "/";
		}
		
	}
	
	public void setScanPackage(String pkg) {
		this.scanPkg = pkg;
	}
	
	public List<GateDefine> scan() throws Exception {
		String scanpath = binPath + scanPkg.replaceAll("\\.", "/");
		return Files.list(FileSystems.getDefault().getPath(scanpath))
				.peek(System.out::println)
			.filter((path)->!path.toFile().isDirectory() && path.toFile().getName().endsWith(".class")) //path.endsWith(".class")
//			.peek(System.out::println)
			.map((path)->path.toFile().getName())
			.map(file->scanClass(scanPkg + "." + getName(file)))
			.collect(ArrayList<GateDefine>::new,
					ArrayList::addAll,
					ArrayList::addAll);
	}
	
	private String getName(String fullname) {
		Matcher matcher = classnamePattern.matcher(fullname);
		if(matcher.matches()) {
			return matcher.group(1);
		}
		throw new IllegalArgumentException(fullname + " is not a full classname");
	}
	
	private List<GateDefine> scanClass(String classname) {
		try{
			System.out.println("scanclass: " + classname);
			Class<?> clazz = Class.forName(classname);
			List<Method> methodList = new ArrayList<>();
			Collections.addAll(methodList, clazz.getMethods());
			return methodList.parallelStream()
				.filter((m) -> m.isAnnotationPresent(Gate.class))
				.map((m)->createGateDefine(clazz, m))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private GateDefine createGateDefine(Class<?> clazz, Method method) {
		Gate gate = method.getDeclaredAnnotation(Gate.class);
		FieldDescriptor desc = null;
		try{
			Descriptor descriptor = (Descriptor) messageClass.getMethod("getDescriptor").invoke(null);
			desc = descriptor.findFieldByNumber(gate.fieldid());
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Parameter[] params = method.getParameters();
		ParamDefine[] paramDefines = new ParamDefine[params.length];
		for(int i=0;i<params.length;i++) {
			Parameter p = params[i];
			if(p.isAnnotationPresent(Param.class)) {
				int fieldId = p.getAnnotation(Param.class).fieldid();
				ParamDefine paramDefine = new ParamDefine(fieldId);
				if(fieldId >= 0) {
					try{
						Descriptor descriptor = (Descriptor) gate.data().getMethod("getDescriptor").invoke(null);
						paramDefine.fieldDescriptor = descriptor.findFieldByNumber(fieldId);
					}catch(Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				}
				paramDefines[i] = paramDefine;
			}
		}
		Object obj = context.getBean(clazz);
		return new GateDefine(gate.msgid(), gate.data(), obj, method, paramDefines, desc);
	}
	
}
