# MakuBoot


**简介**:MakuBoot


**HOST**:http://localhost:8080


**联系人**:阿沐 babamu@126.com


**Version**:2.0


**接口路径**:/v3/api-docs/MakuBoot


[TOC]






# 用户管理


## 保存


**接口地址**:`/sys/user`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "username": "",
  "password": "",
  "realName": "",
  "avatar": "",
  "gender": 0,
  "email": "",
  "mobile": "",
  "orgId": 0,
  "status": 0,
  "roleIdList": [],
  "postIdList": [],
  "superAdmin": 0,
  "orgName": "",
  "createTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysUserVO|用户|body|true|SysUserVO|SysUserVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;password|密码||false|string||
|&emsp;&emsp;realName|姓名||true|string||
|&emsp;&emsp;avatar|头像||false|string||
|&emsp;&emsp;gender|性别 0：男   1：女   2：未知||true|integer(int32)||
|&emsp;&emsp;email|邮箱||false|string||
|&emsp;&emsp;mobile|手机号||true|string||
|&emsp;&emsp;orgId|机构ID||true|integer(int64)||
|&emsp;&emsp;status|状态 0：停用    1：正常||true|integer(int32)||
|&emsp;&emsp;roleIdList|角色ID列表||false|array|integer|
|&emsp;&emsp;postIdList|岗位ID列表||false|array|integer|
|&emsp;&emsp;superAdmin|超级管理员   0：否   1：是||false|integer(int32)||
|&emsp;&emsp;orgName|机构名称||false|string||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改


**接口地址**:`/sys/user`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "username": "",
  "password": "",
  "realName": "",
  "avatar": "",
  "gender": 0,
  "email": "",
  "mobile": "",
  "orgId": 0,
  "status": 0,
  "roleIdList": [],
  "postIdList": [],
  "superAdmin": 0,
  "orgName": "",
  "createTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysUserVO|用户|body|true|SysUserVO|SysUserVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;password|密码||false|string||
|&emsp;&emsp;realName|姓名||true|string||
|&emsp;&emsp;avatar|头像||false|string||
|&emsp;&emsp;gender|性别 0：男   1：女   2：未知||true|integer(int32)||
|&emsp;&emsp;email|邮箱||false|string||
|&emsp;&emsp;mobile|手机号||true|string||
|&emsp;&emsp;orgId|机构ID||true|integer(int64)||
|&emsp;&emsp;status|状态 0：停用    1：正常||true|integer(int32)||
|&emsp;&emsp;roleIdList|角色ID列表||false|array|integer|
|&emsp;&emsp;postIdList|岗位ID列表||false|array|integer|
|&emsp;&emsp;superAdmin|超级管理员   0：否   1：是||false|integer(int32)||
|&emsp;&emsp;orgName|机构名称||false|string||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 删除


**接口地址**:`/sys/user`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改密码


**接口地址**:`/sys/user/password`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "password": "",
  "newPassword": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysUserPasswordVO|用户修改密码|body|true|SysUserPasswordVO|SysUserPasswordVO|
|&emsp;&emsp;password|原密码||true|string||
|&emsp;&emsp;newPassword|新密码，密码长度为 4-20 位||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 导入用户


**接口地址**:`/sys/user/import`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|file||query|true|file||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 信息


**接口地址**:`/sys/user/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysUserVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysUserVO|SysUserVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;password|密码|string||
|&emsp;&emsp;realName|姓名|string||
|&emsp;&emsp;avatar|头像|string||
|&emsp;&emsp;gender|性别 0：男   1：女   2：未知|integer(int32)||
|&emsp;&emsp;email|邮箱|string||
|&emsp;&emsp;mobile|手机号|string||
|&emsp;&emsp;orgId|机构ID|integer(int64)||
|&emsp;&emsp;status|状态 0：停用    1：正常|integer(int32)||
|&emsp;&emsp;roleIdList|角色ID列表|array|integer|
|&emsp;&emsp;postIdList|岗位ID列表|array|integer|
|&emsp;&emsp;superAdmin|超级管理员   0：否   1：是|integer(int32)||
|&emsp;&emsp;orgName|机构名称|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"username": "",
		"password": "",
		"realName": "",
		"avatar": "",
		"gender": 0,
		"email": "",
		"mobile": "",
		"orgId": 0,
		"status": 0,
		"roleIdList": [],
		"postIdList": [],
		"superAdmin": 0,
		"orgName": "",
		"createTime": ""
	}
}
```


## 分页


**接口地址**:`/sys/user/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|用户查询|query|true|SysUserQuery|SysUserQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;username|用户名||false|string||
|&emsp;&emsp;mobile|手机号||false|string||
|&emsp;&emsp;gender|性别||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysUserVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysUserVO|PageResultSysUserVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysUserVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;&emsp;&emsp;password|密码|string||
|&emsp;&emsp;&emsp;&emsp;realName|姓名|string||
|&emsp;&emsp;&emsp;&emsp;avatar|头像|string||
|&emsp;&emsp;&emsp;&emsp;gender|性别 0：男   1：女   2：未知|integer||
|&emsp;&emsp;&emsp;&emsp;email|邮箱|string||
|&emsp;&emsp;&emsp;&emsp;mobile|手机号|string||
|&emsp;&emsp;&emsp;&emsp;orgId|机构ID|integer||
|&emsp;&emsp;&emsp;&emsp;status|状态 0：停用    1：正常|integer||
|&emsp;&emsp;&emsp;&emsp;roleIdList|角色ID列表|array|integer|
|&emsp;&emsp;&emsp;&emsp;postIdList|岗位ID列表|array|integer|
|&emsp;&emsp;&emsp;&emsp;superAdmin|超级管理员   0：否   1：是|integer||
|&emsp;&emsp;&emsp;&emsp;orgName|机构名称|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"username": "",
				"password": "",
				"realName": "",
				"avatar": "",
				"gender": 0,
				"email": "",
				"mobile": "",
				"orgId": 0,
				"status": 0,
				"roleIdList": [],
				"postIdList": [],
				"superAdmin": 0,
				"orgName": "",
				"createTime": ""
			}
		]
	}
}
```


## 登录用户


**接口地址**:`/sys/user/info`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysUserVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysUserVO|SysUserVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;password|密码|string||
|&emsp;&emsp;realName|姓名|string||
|&emsp;&emsp;avatar|头像|string||
|&emsp;&emsp;gender|性别 0：男   1：女   2：未知|integer(int32)||
|&emsp;&emsp;email|邮箱|string||
|&emsp;&emsp;mobile|手机号|string||
|&emsp;&emsp;orgId|机构ID|integer(int64)||
|&emsp;&emsp;status|状态 0：停用    1：正常|integer(int32)||
|&emsp;&emsp;roleIdList|角色ID列表|array|integer|
|&emsp;&emsp;postIdList|岗位ID列表|array|integer|
|&emsp;&emsp;superAdmin|超级管理员   0：否   1：是|integer(int32)||
|&emsp;&emsp;orgName|机构名称|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"username": "",
		"password": "",
		"realName": "",
		"avatar": "",
		"gender": 0,
		"email": "",
		"mobile": "",
		"orgId": 0,
		"status": 0,
		"roleIdList": [],
		"postIdList": [],
		"superAdmin": 0,
		"orgName": "",
		"createTime": ""
	}
}
```


## 导出用户


**接口地址**:`/sys/user/export`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 登录日志


## 分页


**接口地址**:`/sys/log/login/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|登录日志查询|query|true|SysLogLoginQuery|SysLogLoginQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;username|用户名||false|string||
|&emsp;&emsp;address|登录地点||false|string||
|&emsp;&emsp;status|登录状态  0：失败   1：成功||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysLogLoginVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysLogLoginVO|PageResultSysLogLoginVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysLogLoginVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;&emsp;&emsp;ip|登录IP|string||
|&emsp;&emsp;&emsp;&emsp;address|登录地点|string||
|&emsp;&emsp;&emsp;&emsp;userAgent|User Agent|string||
|&emsp;&emsp;&emsp;&emsp;status|登录状态  0：失败   1：成功|integer||
|&emsp;&emsp;&emsp;&emsp;operation|操作信息   0：登录成功   1：退出成功  2：验证码错误  3：账号密码错误|integer||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"username": "",
				"ip": "",
				"address": "",
				"userAgent": "",
				"status": 0,
				"operation": 0,
				"createTime": ""
			}
		]
	}
}
```


## 导出excel


**接口地址**:`/sys/log/login/export`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 机构管理


## 保存


**接口地址**:`/sys/org`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "pid": 0,
  "children": [
    {
      "id": 0,
      "pid": 0,
      "children": [],
      "name": "",
      "sort": 0,
      "createTime": "",
      "parentName": ""
    }
  ],
  "name": "",
  "sort": 0,
  "createTime": "",
  "parentName": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysOrgVO|机构|body|true|SysOrgVO|SysOrgVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;pid|上级ID||true|integer(int64)||
|&emsp;&emsp;children|||false|array|SysOrgVO|
|&emsp;&emsp;name|机构名称||true|string||
|&emsp;&emsp;sort|排序||true|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;parentName|上级名称||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改


**接口地址**:`/sys/org`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "pid": 0,
  "children": [
    {
      "id": 0,
      "pid": 0,
      "children": [],
      "name": "",
      "sort": 0,
      "createTime": "",
      "parentName": ""
    }
  ],
  "name": "",
  "sort": 0,
  "createTime": "",
  "parentName": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysOrgVO|机构|body|true|SysOrgVO|SysOrgVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;pid|上级ID||true|integer(int64)||
|&emsp;&emsp;children|||false|array|SysOrgVO|
|&emsp;&emsp;name|机构名称||true|string||
|&emsp;&emsp;sort|排序||true|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;parentName|上级名称||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 信息


**接口地址**:`/sys/org/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysOrgVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysOrgVO|SysOrgVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;pid|上级ID|integer(int64)||
|&emsp;&emsp;children||array|SysOrgVO|
|&emsp;&emsp;name|机构名称|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;parentName|上级名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"pid": 0,
		"children": [],
		"name": "",
		"sort": 0,
		"createTime": "",
		"parentName": ""
	}
}
```


## 删除


**接口地址**:`/sys/org/{id}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 列表


**接口地址**:`/sys/org/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultListSysOrgVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|array|SysOrgVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;pid|上级ID|integer(int64)||
|&emsp;&emsp;children||array|SysOrgVO|
|&emsp;&emsp;name|机构名称|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;parentName|上级名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 0,
			"pid": 0,
			"children": [],
			"name": "",
			"sort": 0,
			"createTime": "",
			"parentName": ""
		}
	]
}
```


# 菜单管理


## 保存


**接口地址**:`/sys/menu`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "pid": 0,
  "children": [
    {
      "id": 0,
      "pid": 0,
      "children": [],
      "name": "",
      "url": "",
      "type": 0,
      "openStyle": 0,
      "icon": "",
      "authority": "",
      "sort": 0,
      "createTime": "",
      "parentName": ""
    }
  ],
  "name": "",
  "url": "",
  "type": 0,
  "openStyle": 0,
  "icon": "",
  "authority": "",
  "sort": 0,
  "createTime": "",
  "parentName": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysMenuVO|菜单|body|true|SysMenuVO|SysMenuVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;pid|上级ID||true|integer(int64)||
|&emsp;&emsp;children|||false|array|SysMenuVO|
|&emsp;&emsp;name|菜单名称||true|string||
|&emsp;&emsp;url|菜单URL||false|string||
|&emsp;&emsp;type|类型  0：菜单   1：按钮   2：接口||false|integer(int32)||
|&emsp;&emsp;openStyle|打开方式   0：内部   1：外部||false|integer(int32)||
|&emsp;&emsp;icon|菜单图标||false|string||
|&emsp;&emsp;authority|授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)||false|string||
|&emsp;&emsp;sort|排序||false|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;parentName|上级菜单名称||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改


**接口地址**:`/sys/menu`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "pid": 0,
  "children": [
    {
      "id": 0,
      "pid": 0,
      "children": [],
      "name": "",
      "url": "",
      "type": 0,
      "openStyle": 0,
      "icon": "",
      "authority": "",
      "sort": 0,
      "createTime": "",
      "parentName": ""
    }
  ],
  "name": "",
  "url": "",
  "type": 0,
  "openStyle": 0,
  "icon": "",
  "authority": "",
  "sort": 0,
  "createTime": "",
  "parentName": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysMenuVO|菜单|body|true|SysMenuVO|SysMenuVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;pid|上级ID||true|integer(int64)||
|&emsp;&emsp;children|||false|array|SysMenuVO|
|&emsp;&emsp;name|菜单名称||true|string||
|&emsp;&emsp;url|菜单URL||false|string||
|&emsp;&emsp;type|类型  0：菜单   1：按钮   2：接口||false|integer(int32)||
|&emsp;&emsp;openStyle|打开方式   0：内部   1：外部||false|integer(int32)||
|&emsp;&emsp;icon|菜单图标||false|string||
|&emsp;&emsp;authority|授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)||false|string||
|&emsp;&emsp;sort|排序||false|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;parentName|上级菜单名称||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 信息


**接口地址**:`/sys/menu/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysMenuVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysMenuVO|SysMenuVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;pid|上级ID|integer(int64)||
|&emsp;&emsp;children||array|SysMenuVO|
|&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;url|菜单URL|string||
|&emsp;&emsp;type|类型  0：菜单   1：按钮   2：接口|integer(int32)||
|&emsp;&emsp;openStyle|打开方式   0：内部   1：外部|integer(int32)||
|&emsp;&emsp;icon|菜单图标|string||
|&emsp;&emsp;authority|授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;parentName|上级菜单名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"pid": 0,
		"children": [],
		"name": "",
		"url": "",
		"type": 0,
		"openStyle": 0,
		"icon": "",
		"authority": "",
		"sort": 0,
		"createTime": "",
		"parentName": ""
	}
}
```


## 删除


**接口地址**:`/sys/menu/{id}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 菜单导航


**接口地址**:`/sys/menu/nav`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultListSysMenuVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|array|SysMenuVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;pid|上级ID|integer(int64)||
|&emsp;&emsp;children||array|SysMenuVO|
|&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;url|菜单URL|string||
|&emsp;&emsp;type|类型  0：菜单   1：按钮   2：接口|integer(int32)||
|&emsp;&emsp;openStyle|打开方式   0：内部   1：外部|integer(int32)||
|&emsp;&emsp;icon|菜单图标|string||
|&emsp;&emsp;authority|授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;parentName|上级菜单名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 0,
			"pid": 0,
			"children": [],
			"name": "",
			"url": "",
			"type": 0,
			"openStyle": 0,
			"icon": "",
			"authority": "",
			"sort": 0,
			"createTime": "",
			"parentName": ""
		}
	]
}
```


## 菜单列表


**接口地址**:`/sys/menu/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|type|菜单类型 0：菜单 1：按钮  2：接口  null：全部|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultListSysMenuVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|array|SysMenuVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;pid|上级ID|integer(int64)||
|&emsp;&emsp;children||array|SysMenuVO|
|&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;url|菜单URL|string||
|&emsp;&emsp;type|类型  0：菜单   1：按钮   2：接口|integer(int32)||
|&emsp;&emsp;openStyle|打开方式   0：内部   1：外部|integer(int32)||
|&emsp;&emsp;icon|菜单图标|string||
|&emsp;&emsp;authority|授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;parentName|上级菜单名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 0,
			"pid": 0,
			"children": [],
			"name": "",
			"url": "",
			"type": 0,
			"openStyle": 0,
			"icon": "",
			"authority": "",
			"sort": 0,
			"createTime": "",
			"parentName": ""
		}
	]
}
```


## 用户权限标识


**接口地址**:`/sys/menu/authority`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSetString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|array||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": []
}
```


# 岗位管理


## 保存


**接口地址**:`/sys/post`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "postCode": "",
  "postName": "",
  "sort": 0,
  "status": 0,
  "createTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysPostVO|岗位管理|body|true|SysPostVO|SysPostVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;postCode|岗位编码||true|string||
|&emsp;&emsp;postName|岗位名称||true|string||
|&emsp;&emsp;sort|排序||true|integer(int32)||
|&emsp;&emsp;status|状态  0：停用   1：正常||true|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改


**接口地址**:`/sys/post`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "postCode": "",
  "postName": "",
  "sort": 0,
  "status": 0,
  "createTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysPostVO|岗位管理|body|true|SysPostVO|SysPostVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;postCode|岗位编码||true|string||
|&emsp;&emsp;postName|岗位名称||true|string||
|&emsp;&emsp;sort|排序||true|integer(int32)||
|&emsp;&emsp;status|状态  0：停用   1：正常||true|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 删除


**接口地址**:`/sys/post`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 信息


**接口地址**:`/sys/post/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysPostVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysPostVO|SysPostVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;postCode|岗位编码|string||
|&emsp;&emsp;postName|岗位名称|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;status|状态  0：停用   1：正常|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"postCode": "",
		"postName": "",
		"sort": 0,
		"status": 0,
		"createTime": ""
	}
}
```


## 分页


**接口地址**:`/sys/post/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|岗位管理查询|query|true|SysPostQuery|SysPostQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;postCode|岗位编码||false|string||
|&emsp;&emsp;postName|岗位名称||false|string||
|&emsp;&emsp;status|状态  0：停用   1：正常||false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysPostVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysPostVO|PageResultSysPostVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysPostVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;postCode|岗位编码|string||
|&emsp;&emsp;&emsp;&emsp;postName|岗位名称|string||
|&emsp;&emsp;&emsp;&emsp;sort|排序|integer||
|&emsp;&emsp;&emsp;&emsp;status|状态  0：停用   1：正常|integer||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"postCode": "",
				"postName": "",
				"sort": 0,
				"status": 0,
				"createTime": ""
			}
		]
	}
}
```


## 列表


**接口地址**:`/sys/post/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultListSysPostVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|array|SysPostVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;postCode|岗位编码|string||
|&emsp;&emsp;postName|岗位名称|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;status|状态  0：停用   1：正常|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 0,
			"postCode": "",
			"postName": "",
			"sort": 0,
			"status": 0,
			"createTime": ""
		}
	]
}
```


# 认证管理


## 发送短信验证码


**接口地址**:`/sys/auth/send/code`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|mobile||query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 手机号登录


**接口地址**:`/sys/auth/mobile`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "mobile": "",
  "code": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysMobileLoginVO|手机号登录|body|true|SysMobileLoginVO|SysMobileLoginVO|
|&emsp;&emsp;mobile|手机号||false|string||
|&emsp;&emsp;code|验证码||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysTokenVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysTokenVO|SysTokenVO|
|&emsp;&emsp;access_token|access_token|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"access_token": ""
	}
}
```


## 退出


**接口地址**:`/sys/auth/logout`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 账号密码登录


**接口地址**:`/sys/auth/login`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "username": "",
  "password": "",
  "key": "",
  "captcha": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysAccountLoginVO|账号登录|body|true|SysAccountLoginVO|SysAccountLoginVO|
|&emsp;&emsp;username|用户名||false|string||
|&emsp;&emsp;password|密码||false|string||
|&emsp;&emsp;key|唯一key||false|string||
|&emsp;&emsp;captcha|验证码||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysTokenVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysTokenVO|SysTokenVO|
|&emsp;&emsp;access_token|access_token|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"access_token": ""
	}
}
```


## 验证码


**接口地址**:`/sys/auth/captcha`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysCaptchaVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysCaptchaVO|SysCaptchaVO|
|&emsp;&emsp;key|key|string||
|&emsp;&emsp;image|image base64|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"key": "",
		"image": ""
	}
}
```


# 字典数据


## 保存


**接口地址**:`/sys/dict/data`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "dictTypeId": 0,
  "dictLabel": "",
  "dictValue": "",
  "remark": "",
  "sort": 0,
  "createTime": "",
  "updateTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysDictDataVO|字典数据|body|true|SysDictDataVO|SysDictDataVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;dictTypeId|字典类型ID||true|integer(int64)||
|&emsp;&emsp;dictLabel|字典标签||true|string||
|&emsp;&emsp;dictValue|字典值||false|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;sort|排序||true|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;updateTime|更新时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改


**接口地址**:`/sys/dict/data`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "dictTypeId": 0,
  "dictLabel": "",
  "dictValue": "",
  "remark": "",
  "sort": 0,
  "createTime": "",
  "updateTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysDictDataVO|字典数据|body|true|SysDictDataVO|SysDictDataVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;dictTypeId|字典类型ID||true|integer(int64)||
|&emsp;&emsp;dictLabel|字典标签||true|string||
|&emsp;&emsp;dictValue|字典值||false|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;sort|排序||true|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;updateTime|更新时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 删除


**接口地址**:`/sys/dict/data`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 信息


**接口地址**:`/sys/dict/data/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysDictDataVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysDictDataVO|SysDictDataVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;dictTypeId|字典类型ID|integer(int64)||
|&emsp;&emsp;dictLabel|字典标签|string||
|&emsp;&emsp;dictValue|字典值|string||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;updateTime|更新时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"dictTypeId": 0,
		"dictLabel": "",
		"dictValue": "",
		"remark": "",
		"sort": 0,
		"createTime": "",
		"updateTime": ""
	}
}
```


## 分页


**接口地址**:`/sys/dict/data/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|字典数据查询|query|true|SysDictDataQuery|SysDictDataQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;dictTypeId|字典类型ID||true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysDictDataVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysDictDataVO|PageResultSysDictDataVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysDictDataVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;dictTypeId|字典类型ID|integer||
|&emsp;&emsp;&emsp;&emsp;dictLabel|字典标签|string||
|&emsp;&emsp;&emsp;&emsp;dictValue|字典值|string||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;sort|排序|integer||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;updateTime|更新时间|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"dictTypeId": 0,
				"dictLabel": "",
				"dictValue": "",
				"remark": "",
				"sort": 0,
				"createTime": "",
				"updateTime": ""
			}
		]
	}
}
```


# 字典类型


## 保存


**接口地址**:`/sys/dict/type`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "dictType": "",
  "dictName": "",
  "remark": "",
  "sort": 0,
  "createTime": "",
  "updateTime": "",
  "dictSource": 0,
  "dictSql": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysDictTypeVO|字典类型|body|true|SysDictTypeVO|SysDictTypeVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;dictType|字典类型||true|string||
|&emsp;&emsp;dictName|字典名称||true|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;sort|排序||true|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;updateTime|更新时间||false|string(date-time)||
|&emsp;&emsp;dictSource|来源  0：字典数据  1：动态SQL||false|integer(int32)||
|&emsp;&emsp;dictSql|动态sql||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改


**接口地址**:`/sys/dict/type`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "dictType": "",
  "dictName": "",
  "remark": "",
  "sort": 0,
  "createTime": "",
  "updateTime": "",
  "dictSource": 0,
  "dictSql": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysDictTypeVO|字典类型|body|true|SysDictTypeVO|SysDictTypeVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;dictType|字典类型||true|string||
|&emsp;&emsp;dictName|字典名称||true|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;sort|排序||true|integer(int32)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;updateTime|更新时间||false|string(date-time)||
|&emsp;&emsp;dictSource|来源  0：字典数据  1：动态SQL||false|integer(int32)||
|&emsp;&emsp;dictSql|动态sql||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 删除


**接口地址**:`/sys/dict/type`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 信息


**接口地址**:`/sys/dict/type/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysDictTypeVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysDictTypeVO|SysDictTypeVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;dictName|字典名称|string||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;updateTime|更新时间|string(date-time)||
|&emsp;&emsp;dictSource|来源  0：字典数据  1：动态SQL|integer(int32)||
|&emsp;&emsp;dictSql|动态sql|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"dictType": "",
		"dictName": "",
		"remark": "",
		"sort": 0,
		"createTime": "",
		"updateTime": "",
		"dictSource": 0,
		"dictSql": ""
	}
}
```


## 分页


**接口地址**:`/sys/dict/type/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|字典类型查询|query|true|SysDictTypeQuery|SysDictTypeQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;dictType|字典类型||false|string||
|&emsp;&emsp;dictName|字典名称||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysDictTypeVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysDictTypeVO|PageResultSysDictTypeVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysDictTypeVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;&emsp;&emsp;dictName|字典名称|string||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;sort|排序|integer||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;updateTime|更新时间|string||
|&emsp;&emsp;&emsp;&emsp;dictSource|来源  0：字典数据  1：动态SQL|integer||
|&emsp;&emsp;&emsp;&emsp;dictSql|动态sql|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"dictType": "",
				"dictName": "",
				"remark": "",
				"sort": 0,
				"createTime": "",
				"updateTime": "",
				"dictSource": 0,
				"dictSql": ""
			}
		]
	}
}
```


## 动态SQL数据


**接口地址**:`/sys/dict/type/list/sql`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultDictData|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultDictData|PageResultDictData|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|DictData|
|&emsp;&emsp;&emsp;&emsp;dictLabel|字典标签|string||
|&emsp;&emsp;&emsp;&emsp;dictValue|字典值|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"dictLabel": "",
				"dictValue": ""
			}
		]
	}
}
```


## 全部字典数据


**接口地址**:`/sys/dict/type/all`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultListSysDictVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|array|SysDictVO|
|&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;dataList|字典数据列表|array|DictData|
|&emsp;&emsp;&emsp;&emsp;dictLabel|字典标签|string||
|&emsp;&emsp;&emsp;&emsp;dictValue|字典值|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"dictType": "",
			"dataList": [
				{
					"dictLabel": "",
					"dictValue": ""
				}
			]
		}
	]
}
```


# 文件上传


## 上传


**接口地址**:`/sys/file/upload`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|file||query|true|file||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysFileUploadVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysFileUploadVO|SysFileUploadVO|
|&emsp;&emsp;name|文件名称|string||
|&emsp;&emsp;url|文件地址|string||
|&emsp;&emsp;size|文件大小|integer(int64)||
|&emsp;&emsp;platform|存储平台|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"name": "",
		"url": "",
		"size": 0,
		"platform": ""
	}
}
```


# 角色管理


## 保存


**接口地址**:`/sys/role`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "name": "",
  "remark": "",
  "dataScope": 0,
  "menuIdList": [],
  "orgIdList": [],
  "createTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysRoleVO|角色|body|true|SysRoleVO|SysRoleVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;name|角色名称||true|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;dataScope|数据范围  0：全部数据  1：本机构及子机构数据  2：本机构数据  3：本人数据  4：自定义数据||false|integer(int32)||
|&emsp;&emsp;menuIdList|菜单ID列表||false|array|integer|
|&emsp;&emsp;orgIdList|机构ID列表||false|array|integer|
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改


**接口地址**:`/sys/role`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "name": "",
  "remark": "",
  "dataScope": 0,
  "menuIdList": [],
  "orgIdList": [],
  "createTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysRoleVO|角色|body|true|SysRoleVO|SysRoleVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;name|角色名称||true|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;dataScope|数据范围  0：全部数据  1：本机构及子机构数据  2：本机构数据  3：本人数据  4：自定义数据||false|integer(int32)||
|&emsp;&emsp;menuIdList|菜单ID列表||false|array|integer|
|&emsp;&emsp;orgIdList|机构ID列表||false|array|integer|
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 删除


**接口地址**:`/sys/role`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 数据权限


**接口地址**:`/sys/role/data-scope`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "dataScope": 0,
  "orgIdList": []
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysRoleDataScopeVO|角色数据权限|body|true|SysRoleDataScopeVO|SysRoleDataScopeVO|
|&emsp;&emsp;id|id||true|integer(int64)||
|&emsp;&emsp;dataScope|数据范围  0：全部数据  1：本机构及子机构数据  2：本机构数据  3：本人数据  4：自定义数据||true|integer(int32)||
|&emsp;&emsp;orgIdList|机构ID列表||false|array|integer|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 分配角色给用户列表


**接口地址**:`/sys/role/user/{roleId}`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|roleId||path|true|integer(int64)||
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 删除角色用户


**接口地址**:`/sys/role/user/{roleId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|roleId||path|true|integer(int64)||
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 信息


**接口地址**:`/sys/role/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysRoleVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysRoleVO|SysRoleVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;dataScope|数据范围  0：全部数据  1：本机构及子机构数据  2：本机构数据  3：本人数据  4：自定义数据|integer(int32)||
|&emsp;&emsp;menuIdList|菜单ID列表|array|integer|
|&emsp;&emsp;orgIdList|机构ID列表|array|integer|
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"name": "",
		"remark": "",
		"dataScope": 0,
		"menuIdList": [],
		"orgIdList": [],
		"createTime": ""
	}
}
```


## 角色用户-分页


**接口地址**:`/sys/role/user/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|分配角色查询|query|true|SysRoleUserQuery|SysRoleUserQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;username|用户名||false|string||
|&emsp;&emsp;mobile|手机号||false|string||
|&emsp;&emsp;gender|性别||false|integer(int32)||
|&emsp;&emsp;roleId|角色ID||false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysUserVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysUserVO|PageResultSysUserVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysUserVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;&emsp;&emsp;password|密码|string||
|&emsp;&emsp;&emsp;&emsp;realName|姓名|string||
|&emsp;&emsp;&emsp;&emsp;avatar|头像|string||
|&emsp;&emsp;&emsp;&emsp;gender|性别 0：男   1：女   2：未知|integer||
|&emsp;&emsp;&emsp;&emsp;email|邮箱|string||
|&emsp;&emsp;&emsp;&emsp;mobile|手机号|string||
|&emsp;&emsp;&emsp;&emsp;orgId|机构ID|integer||
|&emsp;&emsp;&emsp;&emsp;status|状态 0：停用    1：正常|integer||
|&emsp;&emsp;&emsp;&emsp;roleIdList|角色ID列表|array|integer|
|&emsp;&emsp;&emsp;&emsp;postIdList|岗位ID列表|array|integer|
|&emsp;&emsp;&emsp;&emsp;superAdmin|超级管理员   0：否   1：是|integer||
|&emsp;&emsp;&emsp;&emsp;orgName|机构名称|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"username": "",
				"password": "",
				"realName": "",
				"avatar": "",
				"gender": 0,
				"email": "",
				"mobile": "",
				"orgId": 0,
				"status": 0,
				"roleIdList": [],
				"postIdList": [],
				"superAdmin": 0,
				"orgName": "",
				"createTime": ""
			}
		]
	}
}
```


## 分页


**接口地址**:`/sys/role/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|角色查询|query|true|SysRoleQuery|SysRoleQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;name|角色名称||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysRoleVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysRoleVO|PageResultSysRoleVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysRoleVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;dataScope|数据范围  0：全部数据  1：本机构及子机构数据  2：本机构数据  3：本人数据  4：自定义数据|integer||
|&emsp;&emsp;&emsp;&emsp;menuIdList|菜单ID列表|array|integer|
|&emsp;&emsp;&emsp;&emsp;orgIdList|机构ID列表|array|integer|
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"name": "",
				"remark": "",
				"dataScope": 0,
				"menuIdList": [],
				"orgIdList": [],
				"createTime": ""
			}
		]
	}
}
```


## 角色菜单


**接口地址**:`/sys/role/menu`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultListSysMenuVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|array|SysMenuVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;pid|上级ID|integer(int64)||
|&emsp;&emsp;children||array|SysMenuVO|
|&emsp;&emsp;name|菜单名称|string||
|&emsp;&emsp;url|菜单URL|string||
|&emsp;&emsp;type|类型  0：菜单   1：按钮   2：接口|integer(int32)||
|&emsp;&emsp;openStyle|打开方式   0：内部   1：外部|integer(int32)||
|&emsp;&emsp;icon|菜单图标|string||
|&emsp;&emsp;authority|授权标识(多个用逗号分隔，如：sys:menu:list,sys:menu:save)|string||
|&emsp;&emsp;sort|排序|integer(int32)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;parentName|上级菜单名称|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 0,
			"pid": 0,
			"children": [],
			"name": "",
			"url": "",
			"type": 0,
			"openStyle": 0,
			"icon": "",
			"authority": "",
			"sort": 0,
			"createTime": "",
			"parentName": ""
		}
	]
}
```


## 列表


**接口地址**:`/sys/role/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultListSysRoleVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|array|SysRoleVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;name|角色名称|string||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;dataScope|数据范围  0：全部数据  1：本机构及子机构数据  2：本机构数据  3：本人数据  4：自定义数据|integer(int32)||
|&emsp;&emsp;menuIdList|菜单ID列表|array|integer|
|&emsp;&emsp;orgIdList|机构ID列表|array|integer|
|&emsp;&emsp;createTime|创建时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": [
		{
			"id": 0,
			"name": "",
			"remark": "",
			"dataScope": 0,
			"menuIdList": [],
			"orgIdList": [],
			"createTime": ""
		}
	]
}
```


# 附件管理


## 保存


**接口地址**:`/sys/attachment`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "name": "",
  "url": "",
  "size": 0,
  "platform": "",
  "createTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysAttachmentVO|附件管理|body|true|SysAttachmentVO|SysAttachmentVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;name|附件名称||false|string||
|&emsp;&emsp;url|附件地址||false|string||
|&emsp;&emsp;size|附件大小||false|integer(int64)||
|&emsp;&emsp;platform|存储平台||false|string||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 删除


**接口地址**:`/sys/attachment`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 分页


**接口地址**:`/sys/attachment/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|附件管理查询|query|true|SysAttachmentQuery|SysAttachmentQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;name|附件名称||false|string||
|&emsp;&emsp;platform|存储平台||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysAttachmentVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysAttachmentVO|PageResultSysAttachmentVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysAttachmentVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;name|附件名称|string||
|&emsp;&emsp;&emsp;&emsp;url|附件地址|string||
|&emsp;&emsp;&emsp;&emsp;size|附件大小|integer||
|&emsp;&emsp;&emsp;&emsp;platform|存储平台|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"name": "",
				"url": "",
				"size": 0,
				"platform": "",
				"createTime": ""
			}
		]
	}
}
```


# 参数管理


## 保存


**接口地址**:`/sys/params`


**请求方式**:`POST`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "paramName": "",
  "paramType": 0,
  "paramKey": "",
  "paramValue": "",
  "remark": "",
  "version": 0,
  "deleted": 0,
  "creator": 0,
  "createTime": "",
  "updater": 0,
  "updateTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysParamsVO|参数管理|body|true|SysParamsVO|SysParamsVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;paramName|参数名称||false|string||
|&emsp;&emsp;paramType|系统参数||false|integer(int32)||
|&emsp;&emsp;paramKey|参数键||false|string||
|&emsp;&emsp;paramValue|参数值||false|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;version|版本号||false|integer(int32)||
|&emsp;&emsp;deleted|删除标识||false|integer(int32)||
|&emsp;&emsp;creator|创建者||false|integer(int64)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;updater|更新者||false|integer(int64)||
|&emsp;&emsp;updateTime|更新时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 修改


**接口地址**:`/sys/params`


**请求方式**:`PUT`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "paramName": "",
  "paramType": 0,
  "paramKey": "",
  "paramValue": "",
  "remark": "",
  "version": 0,
  "deleted": 0,
  "creator": 0,
  "createTime": "",
  "updater": 0,
  "updateTime": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|sysParamsVO|参数管理|body|true|SysParamsVO|SysParamsVO|
|&emsp;&emsp;id|id||false|integer(int64)||
|&emsp;&emsp;paramName|参数名称||false|string||
|&emsp;&emsp;paramType|系统参数||false|integer(int32)||
|&emsp;&emsp;paramKey|参数键||false|string||
|&emsp;&emsp;paramValue|参数值||false|string||
|&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;version|版本号||false|integer(int32)||
|&emsp;&emsp;deleted|删除标识||false|integer(int32)||
|&emsp;&emsp;creator|创建者||false|integer(int64)||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;updater|更新者||false|integer(int64)||
|&emsp;&emsp;updateTime|更新时间||false|string(date-time)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 删除


**接口地址**:`/sys/params`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded,application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|integers|integer|body|true|array||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


## 信息


**接口地址**:`/sys/params/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id||path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultSysParamsVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||SysParamsVO|SysParamsVO|
|&emsp;&emsp;id|id|integer(int64)||
|&emsp;&emsp;paramName|参数名称|string||
|&emsp;&emsp;paramType|系统参数|integer(int32)||
|&emsp;&emsp;paramKey|参数键|string||
|&emsp;&emsp;paramValue|参数值|string||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;version|版本号|integer(int32)||
|&emsp;&emsp;deleted|删除标识|integer(int32)||
|&emsp;&emsp;creator|创建者|integer(int64)||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;updater|更新者|integer(int64)||
|&emsp;&emsp;updateTime|更新时间|string(date-time)||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"id": 0,
		"paramName": "",
		"paramType": 0,
		"paramKey": "",
		"paramValue": "",
		"remark": "",
		"version": 0,
		"deleted": 0,
		"creator": 0,
		"createTime": "",
		"updater": 0,
		"updateTime": ""
	}
}
```


## 分页


**接口地址**:`/sys/params/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|query|参数管理查询|query|true|SysParamsQuery|SysParamsQuery|
|&emsp;&emsp;page|当前页码||true|integer(int32)||
|&emsp;&emsp;limit|每页条数||true|integer(int32)||
|&emsp;&emsp;order|排序字段||false|string||
|&emsp;&emsp;asc|是否升序||false|boolean||
|&emsp;&emsp;paramType|系统参数||false|integer(int32)||
|&emsp;&emsp;paramKey|参数键||false|string||
|&emsp;&emsp;paramValue|参数值||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultPageResultSysParamsVO|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data||PageResultSysParamsVO|PageResultSysParamsVO|
|&emsp;&emsp;total|总记录数|integer(int32)||
|&emsp;&emsp;list|列表数据|array|SysParamsVO|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;paramName|参数名称|string||
|&emsp;&emsp;&emsp;&emsp;paramType|系统参数|integer||
|&emsp;&emsp;&emsp;&emsp;paramKey|参数键|string||
|&emsp;&emsp;&emsp;&emsp;paramValue|参数值|string||
|&emsp;&emsp;&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;&emsp;&emsp;version|版本号|integer||
|&emsp;&emsp;&emsp;&emsp;deleted|删除标识|integer||
|&emsp;&emsp;&emsp;&emsp;creator|创建者|integer||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;updater|更新者|integer||
|&emsp;&emsp;&emsp;&emsp;updateTime|更新时间|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": {
		"total": 0,
		"list": [
			{
				"id": 0,
				"paramName": "",
				"paramType": 0,
				"paramKey": "",
				"paramValue": "",
				"remark": "",
				"version": 0,
				"deleted": 0,
				"creator": 0,
				"createTime": "",
				"updater": 0,
				"updateTime": ""
			}
		]
	}
}
```


# 新模块测试


## 测试接口


**接口地址**:`/new/test`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResultString|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|编码 0表示成功，其他值表示失败|integer(int32)|integer(int32)|
|msg|消息内容|string||
|data|响应数据|string||


**响应示例**:
```javascript
{
	"code": 0,
	"msg": "",
	"data": ""
}
```


# index-controller


## index


**接口地址**:`/`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```