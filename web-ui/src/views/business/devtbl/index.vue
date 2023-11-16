<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
<!--      <el-form-item prop="sn">-->
<!--        <el-input v-model="queryParams.sn" placeholder="请输入产品SN；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="devnickname">-->
<!--        <el-input v-model="queryParams.devnickname" placeholder="请输入昵称；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item prop="devpubid">
        <el-input v-model="queryParams.devpubid" placeholder="请输入一级设备账号；" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
<!--      <el-form-item prop="devpubidfortel">-->
<!--        <el-input v-model="queryParams.devpubidfortel" placeholder="请输入一级设备账号，专为电话" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="devpubid3">-->
<!--        <el-input v-model="queryParams.devpubid3" placeholder="请输入一级设备账号，专为其它" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="passwd">-->
<!--        <el-input v-model="queryParams.passwd" placeholder="请输入密码" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="alive">-->
<!--        <el-input v-model="queryParams.alive" placeholder="请输入注册状态；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="updatetime">-->
<!--        <el-date-picker clearable v-model="queryParams.updatetime" type="date" value-format="yyyy-MM-dd" placeholder="请选择在线更新时间；">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="homename">-->
<!--        <el-input v-model="queryParams.homename" placeholder="请输入注册主域(服务器名+服务器IP+服务器SipPort)；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="domain">-->
<!--        <el-input v-model="queryParams.domain" placeholder="请输入域名；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="port">-->
<!--        <el-input v-model="queryParams.port" placeholder="请输入端口；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="ip">-->
<!--        <el-input v-model="queryParams.ip" placeholder="请输入IP地址 ；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="natip">-->
<!--        <el-input v-model="queryParams.natip" placeholder="请输入穿网IP地址；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="webportal">-->
<!--        <el-input v-model="queryParams.webportal" placeholder="请输入网页入口；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="webloginuser">-->
<!--        <el-input v-model="queryParams.webloginuser" placeholder="请输入网页入口登录用户；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="webloginpasswd">-->
<!--        <el-input v-model="queryParams.webloginpasswd" placeholder="请输入网页入口登录密码；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="totaldiskroom">-->
<!--        <el-input v-model="queryParams.totaldiskroom" placeholder="请输入录象总空间；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="diskroom">-->
<!--        <el-input v-model="queryParams.diskroom" placeholder="请输入录像剩余空间；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="areaid">-->
<!--        <el-input v-model="queryParams.areaid" placeholder="请输入所属区域(DevAreaTbl的ID,设备区域)；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="model">-->
<!--        <el-input v-model="queryParams.model" placeholder="请输入设备型号(DevModelTbl的Model2)" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="sysver">-->
<!--        <el-input v-model="queryParams.sysver" placeholder="请输入所属平台版本  " clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="mrspubid">-->
<!--        <el-input v-model="queryParams.mrspubid" placeholder="请输入归属分发服务器" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="msspubid">-->
<!--        <el-input v-model="queryParams.msspubid" placeholder="请输入归属存储服务器" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="vodpubid">-->
<!--        <el-input v-model="queryParams.vodpubid" placeholder="请输入归属点播服务器" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="sipgwpubid">-->
<!--        <el-input v-model="queryParams.sipgwpubid" placeholder="请输入归属网关服务器；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="acspubid">-->
<!--        <el-input v-model="queryParams.acspubid" placeholder="请输入归属报警服务器" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="p2pmaxconns">-->
<!--        <el-input v-model="queryParams.p2pmaxconns" placeholder="请输入点对点最大连接数" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="relaymode">-->
<!--        <el-input v-model="queryParams.relaymode" placeholder="请输入分发连接方式(0-UDP单播；1-UDP组播)" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="opendate">-->
<!--        <el-date-picker clearable v-model="queryParams.opendate" type="date" value-format="yyyy-MM-dd" placeholder="请选择安装日期；">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="installlocation">-->
<!--        <el-input v-model="queryParams.installlocation" placeholder="请输入安装详细地点；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="linktelphone">-->
<!--        <el-input v-model="queryParams.linktelphone" placeholder="请输入电话；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="cert">-->
<!--        <el-input v-model="queryParams.cert" placeholder="请输入证件名称(身份证-1；驾驶证-2；军人证-3)；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="certnum">-->
<!--        <el-input v-model="queryParams.certnum" placeholder="请输入证件编号；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="opentime">-->
<!--        <el-date-picker clearable v-model="queryParams.opentime" type="date" value-format="yyyy-MM-dd" placeholder="请选择开户日期；">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="closetime">-->
<!--        <el-date-picker clearable v-model="queryParams.closetime" type="date" value-format="yyyy-MM-dd" placeholder="请选择销户日期；">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="longitude">-->
<!--        <el-input v-model="queryParams.longitude" placeholder="请输入经度；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="latitude">-->
<!--        <el-input v-model="queryParams.latitude" placeholder="请输入纬度；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="memo">-->
<!--        <el-input v-model="queryParams.memo" placeholder="请输入设备备忘录；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="dmarker">-->
<!--        <el-input v-model="queryParams.dmarker" placeholder="请输入0-未变化；1-增加；2-删除；3-修改；4-已同步，可删除；" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="storageid">-->
<!--        <el-input v-model="queryParams.storageid" placeholder="请输入${comment}" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
<!--      <el-form-item prop="platid">-->
<!--        <el-input v-model="queryParams.platid" placeholder="请输入${comment}" clearable @keyup.enter.native="handleQuery"/>-->
<!--      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">-->
<!--        <el-button type="primary" plain icon="el-icon-plus" size="small" @click="handleAdd" v-hasPermi="['business:devtbl:add']">新增</el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button type="success" plain icon="el-icon-edit" size="small" :disabled="single" @click="handleUpdate" v-hasPermi="['business:devtbl:edit']">修改</el-button>-->
<!--      </el-col>-->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="small" :disabled="multiple" @click="handleDelete" v-hasPermi="['business:devtbl:remove']">删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button type="warning" plain icon="el-icon-download" size="small" @click="handleExport" v-hasPermi="['business:devtbl:export']">导出</el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="devtblList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="自增ID；" align="center" prop="id"/>
      <el-table-column label="产品SN；" align="center" prop="sn"/>
      <el-table-column label="昵称；" align="center" prop="devnickname"/>
      <el-table-column label="一级设备账号；" align="center" prop="devpubid"/>
      <el-table-column label="一级设备账号，专为电话" align="center" prop="devpubidfortel"/>
      <el-table-column label="一级设备账号，专为其它" align="center" prop="devpubid3"/>
      <el-table-column label="密码" align="center" prop="passwd"/>
      <el-table-column label="注册状态；" align="center" prop="alive"/>
      <el-table-column label="在线更新时间；" align="center" prop="updatetime" width="155">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatetime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="注册主域(服务器名+服务器IP+服务器SipPort)；" align="center" prop="homename"/>
      <el-table-column label="域名；" align="center" prop="domain"/>
      <el-table-column label="端口；" align="center" prop="port"/>
      <el-table-column label="IP地址 ；" align="center" prop="ip"/>
      <el-table-column label="穿网IP地址；" align="center" prop="natip"/>
      <el-table-column label="网页入口；" align="center" prop="webportal"/>
      <el-table-column label="网页入口登录用户；" align="center" prop="webloginuser"/>
      <el-table-column label="网页入口登录密码；" align="center" prop="webloginpasswd"/>
      <el-table-column label="录象总空间；" align="center" prop="totaldiskroom"/>
      <el-table-column label="录像剩余空间；" align="center" prop="diskroom"/>
      <el-table-column label="所属区域(DevAreaTbl的ID,设备区域)；" align="center" prop="areaid"/>
      <el-table-column label="设备型号(DevModelTbl的Model2)" align="center" prop="model"/>
      <el-table-column label="所属平台版本  " align="center" prop="sysver"/>
      <el-table-column label="归属分发服务器" align="center" prop="mrspubid"/>
      <el-table-column label="归属存储服务器" align="center" prop="msspubid"/>
      <el-table-column label="归属点播服务器" align="center" prop="vodpubid"/>
      <el-table-column label="归属网关服务器；" align="center" prop="sipgwpubid"/>
      <el-table-column label="归属报警服务器" align="center" prop="acspubid"/>
      <el-table-column label="点对点最大连接数" align="center" prop="p2pmaxconns"/>
      <el-table-column label="分发连接方式(0-UDP单播；1-UDP组播)" align="center" prop="relaymode"/>
      <el-table-column label="安装日期；" align="center" prop="opendate" width="155">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.opendate) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="安装详细地点；" align="center" prop="installlocation"/>
      <el-table-column label="电话；" align="center" prop="linktelphone"/>
      <el-table-column label="证件名称(身份证-1；驾驶证-2；军人证-3)；" align="center" prop="cert"/>
      <el-table-column label="证件编号；" align="center" prop="certnum"/>
      <el-table-column label="开户日期；" align="center" prop="opentime" width="155">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.opentime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="销户日期；" align="center" prop="closetime" width="155">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.closetime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="有效状态；" align="center" prop="status"/>
      <el-table-column label="经度；" align="center" prop="longitude"/>
      <el-table-column label="纬度；" align="center" prop="latitude"/>
      <el-table-column label="设备备忘录；" align="center" prop="memo"/>
      <el-table-column label="；" align="center" prop="onvifuri"/>
      <el-table-column label="0-未变化；1-增加；2-删除；3-修改；4-已同步，可删除；" align="center" prop="dmarker"/>
      <el-table-column label="${comment}" align="center" prop="storageid"/>
      <el-table-column label="${comment}" align="center" prop="platid"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:devtbl:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color: red;" @click="handleDelete(scope.row)" v-hasPermi="['business:devtbl:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList"/>

    <!-- 添加或修改创建设备对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
          <el-form-item label="产品SN；" prop="sn">
            <el-input v-model="form.sn" placeholder="请输入产品SN；"/>
          </el-form-item>
          <el-form-item label="昵称；" prop="devnickname">
            <el-input v-model="form.devnickname" placeholder="请输入昵称；"/>
          </el-form-item>
          <el-form-item label="一级设备账号；" prop="devpubid">
            <el-input v-model="form.devpubid" placeholder="请输入一级设备账号；"/>
          </el-form-item>
          <el-form-item label="一级设备账号，专为电话" prop="devpubidfortel">
            <el-input v-model="form.devpubidfortel" placeholder="请输入一级设备账号，专为电话"/>
          </el-form-item>
          <el-form-item label="一级设备账号，专为其它" prop="devpubid3">
            <el-input v-model="form.devpubid3" placeholder="请输入一级设备账号，专为其它"/>
          </el-form-item>
          <el-form-item label="密码" prop="passwd">
            <el-input v-model="form.passwd" placeholder="请输入密码"/>
          </el-form-item>
          <el-form-item label="注册状态；" prop="alive">
            <el-input v-model="form.alive" placeholder="请输入注册状态；"/>
          </el-form-item>
          <el-form-item label="更新时间；" prop="updatetime">
          <el-input v-model="form.updatetime" placeholder="请输入更新时间；"/>
          </el-form-item>
          <el-form-item label="注册主域(服务器名+服务器IP+服务器SipPort)；" prop="homename">
            <el-input v-model="form.homename" placeholder="请输入注册主域(服务器名+服务器IP+服务器SipPort)；"/>
          </el-form-item>
          <el-form-item label="域名；" prop="domain">
            <el-input v-model="form.domain" placeholder="请输入域名；"/>
          </el-form-item>
          <el-form-item label="端口；" prop="port">
            <el-input v-model="form.port" placeholder="请输入端口；"/>
          </el-form-item>
          <el-form-item label="IP地址 ；" prop="ip">
            <el-input v-model="form.ip" placeholder="请输入IP地址 ；"/>
          </el-form-item>
          <el-form-item label="穿网IP地址；" prop="natip">
            <el-input v-model="form.natip" placeholder="请输入穿网IP地址；"/>
          </el-form-item>
          <el-form-item label="网页入口；" prop="webportal">
            <el-input v-model="form.webportal" placeholder="请输入网页入口；"/>
          </el-form-item>
          <el-form-item label="网页入口登录用户；" prop="webloginuser">
            <el-input v-model="form.webloginuser" placeholder="请输入网页入口登录用户；"/>
          </el-form-item>
          <el-form-item label="网页入口登录密码；" prop="webloginpasswd">
            <el-input v-model="form.webloginpasswd" placeholder="请输入网页入口登录密码；"/>
          </el-form-item>
          <el-form-item label="录象总空间；" prop="totaldiskroom">
            <el-input v-model="form.totaldiskroom" placeholder="请输入录象总空间；"/>
          </el-form-item>
          <el-form-item label="录像剩余空间；" prop="diskroom">
            <el-input v-model="form.diskroom" placeholder="请输入录像剩余空间；"/>
          </el-form-item>
          <el-form-item label="所属区域(DevAreaTbl的ID,设备区域)；" prop="areaid">
            <el-input v-model="form.areaid" placeholder="请输入所属区域(DevAreaTbl的ID,设备区域)；"/>
          </el-form-item>
          <el-form-item label="设备型号(DevModelTbl的Model2)" prop="model">
            <el-input v-model="form.model" placeholder="请输入设备型号(DevModelTbl的Model2)"/>
          </el-form-item>
          <el-form-item label="所属平台版本  " prop="sysver">
            <el-input v-model="form.sysver" placeholder="请输入所属平台版本  "/>
          </el-form-item>
          <el-form-item label="归属分发服务器" prop="mrspubid">
            <el-input v-model="form.mrspubid" placeholder="请输入归属分发服务器"/>
          </el-form-item>
          <el-form-item label="归属存储服务器" prop="msspubid">
            <el-input v-model="form.msspubid" placeholder="请输入归属存储服务器"/>
          </el-form-item>
          <el-form-item label="归属点播服务器" prop="vodpubid">
            <el-input v-model="form.vodpubid" placeholder="请输入归属点播服务器"/>
          </el-form-item>
          <el-form-item label="归属网关服务器；" prop="sipgwpubid">
            <el-input v-model="form.sipgwpubid" placeholder="请输入归属网关服务器；"/>
          </el-form-item>
          <el-form-item label="归属报警服务器" prop="acspubid">
            <el-input v-model="form.acspubid" placeholder="请输入归属报警服务器"/>
          </el-form-item>
          <el-form-item label="点对点最大连接数" prop="p2pmaxconns">
            <el-input v-model="form.p2pmaxconns" placeholder="请输入点对点最大连接数"/>
          </el-form-item>
          <el-form-item label="分发连接方式(0-UDP单播；1-UDP组播)" prop="relaymode">
            <el-input v-model="form.relaymode" placeholder="请输入分发连接方式(0-UDP单播；1-UDP组播)"/>
          </el-form-item>
          <el-form-item label="安装日期；" prop="opendate">
            <el-date-picker clearable v-model="form.opendate" type="date" value-format="yyyy-MM-dd" placeholder="请选择安装日期；">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="安装详细地点；" prop="installlocation">
            <el-input v-model="form.installlocation" placeholder="请输入安装详细地点；"/>
          </el-form-item>
          <el-form-item label="电话；" prop="linktelphone">
            <el-input v-model="form.linktelphone" placeholder="请输入电话；"/>
          </el-form-item>
          <el-form-item label="证件名称(身份证-1；驾驶证-2；军人证-3)；" prop="cert">
            <el-input v-model="form.cert" placeholder="请输入证件名称(身份证-1；驾驶证-2；军人证-3)；"/>
          </el-form-item>
          <el-form-item label="证件编号；" prop="certnum">
            <el-input v-model="form.certnum" placeholder="请输入证件编号；"/>
          </el-form-item>
          <el-form-item label="开户日期；" prop="opentime">
            <el-date-picker clearable v-model="form.opentime" type="date" value-format="yyyy-MM-dd" placeholder="请选择开户日期；">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="销户日期；" prop="closetime">
            <el-date-picker clearable v-model="form.closetime" type="date" value-format="yyyy-MM-dd" placeholder="请选择销户日期；">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="经度；" prop="longitude">
            <el-input v-model="form.longitude" placeholder="请输入经度；"/>
          </el-form-item>
          <el-form-item label="纬度；" prop="latitude">
            <el-input v-model="form.latitude" placeholder="请输入纬度；"/>
          </el-form-item>
          <el-form-item label="设备备忘录；" prop="memo">
            <el-input v-model="form.memo" placeholder="请输入设备备忘录；"/>
          </el-form-item>
          <el-form-item label="；" prop="onvifuri">
            <el-input v-model="form.onvifuri" type="textarea" placeholder="请输入内容"/>
          </el-form-item>
          <el-form-item label="0-未变化；1-增加；2-删除；3-修改；4-已同步，可删除；" prop="dmarker">
            <el-input v-model="form.dmarker" placeholder="请输入0-未变化；1-增加；2-删除；3-修改；4-已同步，可删除；"/>
          </el-form-item>
          <el-form-item label="${comment}" prop="storageid">
            <el-input v-model="form.storageid" placeholder="请输入${comment}"/>
          </el-form-item>
          <el-form-item label="${comment}" prop="platid">
            <el-input v-model="form.platid" placeholder="请输入${comment}"/>
          </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDevtbl, getDevtbl, delDevtbl, addDevtbl, updateDevtbl } from "@/api/business/devtbl";
import {parseTime} from "@/utils/util";
export default {
  name: "Devtbl",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 创建设备表格数据
      devtblList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sn: null,
        devnickname: null,
        devpubid: null,
        devpubidfortel: null,
        devpubid3: null,
        passwd: null,
        alive: null,
        updatetime: null,
        homename: null,
        domain: null,
        port: null,
        ip: null,
        natip: null,
        webportal: null,
        webloginuser: null,
        webloginpasswd: null,
        totaldiskroom: null,
        diskroom: null,
        areaid: null,
        model: null,
        sysver: null,
        mrspubid: null,
        msspubid: null,
        vodpubid: null,
        sipgwpubid: null,
        acspubid: null,
        p2pmaxconns: null,
        relaymode: null,
        opendate: null,
        installlocation: null,
        linktelphone: null,
        cert: null,
        certnum: null,
        opentime: null,
        closetime: null,
        status: null,
        longitude: null,
        latitude: null,
        memo: null,
        onvifuri: null,
        dmarker: null,
        storageid: null,
        platid: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
              sn: [
          {
            required: true, message: "产品SN；不能为空", trigger: "blur" }
        ],
              devpubid: [
          {
            required: true, message: "一级设备账号；不能为空", trigger: "blur" }
        ],
              updatetime: [
          {
            required: true, message: "在线更新时间；不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询创建设备列表 */
    getList() {
      this.loading = true;
      listDevtbl(this.queryParams).then(response => {
        this.devtblList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        sn: null,
        devnickname: null,
        devpubid: null,
        devpubidfortel: null,
        devpubid3: null,
        passwd: null,
        alive: null,
        updatetime: null,
        homename: null,
        domain: null,
        port: null,
        ip: null,
        natip: null,
        webportal: null,
        webloginuser: null,
        webloginpasswd: null,
        totaldiskroom: null,
        diskroom: null,
        areaid: null,
        model: null,
        sysver: null,
        mrspubid: null,
        msspubid: null,
        vodpubid: null,
        sipgwpubid: null,
        acspubid: null,
        p2pmaxconns: null,
        relaymode: null,
        opendate: null,
        installlocation: null,
        linktelphone: null,
        cert: null,
        certnum: null,
        opentime: null,
        closetime: null,
    status: 0
,
        longitude: null,
        latitude: null,
        memo: null,
        onvifuri: null,
        dmarker: null,
        storageid: null,
        platid: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加创建设备";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id
      getDevtbl(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改创建设备";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id!=null){
              updateDevtbl(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDevtbl(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row && row.id ? [row.id] : this.ids;
      this.$modal.confirm('是否确认删除所选数据项？').then(function () {
        return delDevtbl(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
  /** 导出按钮操作 */
  handleExport(){
    this.download('business/devtbl/export', {...this.queryParams}, `devtbl_${parseTime(new Date().getTime())}.xlsx`)
  }
}
};
</script>
