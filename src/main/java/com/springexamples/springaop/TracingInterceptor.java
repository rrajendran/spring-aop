/**
 *  Copyright 2012 Gunnar Morling (http://www.gunnarmorling.de/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.springexamples.springaop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class TracingInterceptor implements MethodInterceptor {
	public static final Logger LOGGER = LoggerFactory.getLogger("eventlogger");

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object object = null;
		MethodToString methodToString = new MethodToString(invocation.getMethod());
		try {
			LOGGER.info(">>> Started {}", methodToString);
			 object = invocation.proceed();
		}catch (Exception e){
			LOGGER.error("<<< Error :" , e);
		}
		finally {
			LOGGER.info("<<< Completed {}", methodToString);
		}
		return object;
	}

	private static class MethodToString {

		private final Method method;

		private MethodToString(Method method) {
			this.method = method;
		}

		@Override
		public String toString() {

			StringBuilder parameterString = new StringBuilder();

			for (Class<?> oneParameterType : method.getParameterTypes()) {
				if (parameterString.length() > 0) {
					parameterString.append(", ");
				}
				parameterString.append(oneParameterType.getName());
			}

			return method.getName() + "(" + parameterString + ")";
		}
	}
}
