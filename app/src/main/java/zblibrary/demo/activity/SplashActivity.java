/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zblibrary.demo.activity;

import zblibrary.demo.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.pt.shellprogramlibrary.CheckBizBean;
import com.pt.shellprogramlibrary.Constants;
import com.pt.shellprogramlibrary.H5Activity;
import com.pt.shellprogramlibrary.IntentUtils;
import com.pt.shellprogramlibrary.JsonUtils;
import com.pt.shellprogramlibrary.OnHttpResponseListener;
import com.pt.shellprogramlibrary.OnShellProgaramListener;
import com.pt.shellprogramlibrary.ShellProgramUtils;
import com.pt.shellprogramlibrary.UpdateApkActivity;

/**闪屏activity，保证点击桌面应用图标后无延时响应
 * @author Lemon
 */
public class SplashActivity extends Activity implements OnShellProgaramListener{

	private static final String TAG = "SplashActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

	}

	private void init(){
		boolean exist=isInstallApp(Constants.PKG_NAME_ORIGIN);
		if(exist){
			final Intent intent = getPackageManager().getLaunchIntentForPackage(Constants.PKG_NAME_ORIGIN);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}else {
			ShellProgramUtils.startBiz(this, this);
		}
	}

	boolean isInstallApp(String packageName)
	{
		try {
			PackageManager pm=getPackageManager();
			ApplicationInfo info=pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return info!=null;

		} catch (PackageManager.NameNotFoundException e) {
			// TODO: handle exception
			return false;
		}
	}
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

	@Override
	public void onShowWeb(String url) {
		Log.d(TAG, "onShowWeb: ");
		IntentUtils.jumpActivityExtraStr(H5Activity.class,this,url);
		finish();
	}

	@Override
	public void onUpdateApk(String url) {
		IntentUtils.jumpActivityExtraStr(UpdateApkActivity.class,this,url);
		finish();
	}

	@Override
	public void onShowShellApk() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(AboutActivity.createIntent(SplashActivity.this));
				finish();
			}
		}, 500);
	}
}