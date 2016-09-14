/**
 * Copyright (DigitalChina) 2016-2020, DigitalChina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dc.city.common.domain;

import java.io.Serializable;

import com.dc.city.common.utils.ClassUtils;
 

/**
 * 领域对象的基类
 * 重写了equals、hashCode、toString 方法
 * @author tiger
 *
 */
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -3936744392532421052L;
	public abstract Serializable getId();

	/**
	 * 由于hibernte的cglib proxy问题 所以 1、class名字的相等性要通过ClassUtils.equals()进行比较
	 * 2、other对象的id只能通过getId()的方法来取得
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!ClassUtils.equals(this.getClass(), obj.getClass()))
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		if (null == getId()) {
			return super.hashCode();
		}
		return getId().hashCode();
	}
	
	

	 

}
