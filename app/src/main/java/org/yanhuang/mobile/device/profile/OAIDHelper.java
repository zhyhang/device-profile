package org.yanhuang.mobile.device.profile;

import android.content.Context;
import android.util.Log;
import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

import java.util.function.Consumer;

/**
 * OAID获取工具类
 * http://www.msa-alliance.cn/col.jsp?id=120
 */
public class OAIDHelper implements IIdentifierListener {

	private final Consumer<IdSupplier> idsCollector;

	public OAIDHelper(Consumer<IdSupplier> idsCollector) {
		this.idsCollector = idsCollector;
	}

	/**
	 * 触发sdk的device id获取调用
	 *
	 * @param cxt context
	 * @return 错误提示信息
	 */
	public String triggerOAIDRead(Context cxt) {
		long tsb = System.currentTimeMillis();
		// 触发ID获取方法
		int resNum = CallFromReflect(cxt);
		long tse = System.currentTimeMillis();
		long offset = tse - tsb;
		String errMsg = null;
		if (resNum == ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT) {//不支持的设备
			errMsg = "不支持的设备";
		} else if (resNum == ErrorCode.INIT_ERROR_LOAD_CONFIGFILE) {//加载配置文件出错
			errMsg = "加载配置文件出错";
		} else if (resNum == ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT) {//不支持的设备厂商
			errMsg = "不支持的设备厂商";
		} else if (resNum == ErrorCode.INIT_ERROR_RESULT_DELAY) {//获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程
			errMsg = "获取接口是异步的，结果会在回调中返回，回调执行的回调可能在工作线程";
		} else if (resNum == ErrorCode.INIT_HELPER_CALL_ERROR) {//反射调用出错
			errMsg = "反射调用出错";
		}
		Log.d(getClass().getSimpleName(),
				"return value: " + resNum + " message: " + errMsg + " time cost(ms): " + offset);
		return errMsg != null ? errMsg + "（耗时" + offset + "ms）" : null;
	}

	private int CallFromReflect(Context cxt) {
		return MdidSdkHelper.InitSdk(cxt, true, this);
	}


	@Override
	public void OnSupport(boolean isSupport, IdSupplier idSupplier) {
		if (idSupplier == null) {
			return;
		}
		if (idsCollector != null) {
			idsCollector.accept(idSupplier);
		}
	}

}
