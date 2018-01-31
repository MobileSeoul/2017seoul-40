package com.js.sci.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.js.sci.R;

/**
 * Created by jskim on 2016. 8. 4..
 */
public class SCIFragmentController {
    private FragmentManager fragmentManager;
    private int viewId;

    public SCIFragmentController(FragmentActivity activity, int viewId) {
        this.viewId = viewId;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    /**
     * Add the fragment to the activity
     * @param fragment
     */
    public void add(Fragment fragment) {
        add(fragment, null);
    }

    /**
     * Add the fragment to the activity
     * @param fragment
     * @param tag
     */
    public void add(Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (TextUtils.isEmpty(tag)) {
            ft.add(viewId, fragment);
        } else {
            ft.add(viewId, fragment, tag);
        }
//		ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    /**
     * Add the fragment to the activity
     * @param fragment
     */
    public void addWithAnimation(Fragment fragment) {
        addWithAnimation(fragment, null);
    }

    /**
     * Add the fragment to the activity
     * @param fragment
     * @param tag
     */
    public void addWithAnimation(Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_add, R.anim.exit_add);
        if (TextUtils.isEmpty(tag)) {
            ft.add(viewId, fragment);
        } else {
            ft.add(viewId, fragment, tag);
        }
        ft.commitAllowingStateLoss();
    }

    /**
     * Replace the fragment to the activity
     * @param fragment
     */
    public void replace(Fragment fragment) {
        replace(fragment, null);
    }

    /**
     * Replace the fragment to the activity
     * @param fragment
     * @param tag
     */
    public void replace(Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (TextUtils.isEmpty(tag)) {
            ft.replace(viewId, fragment);
        } else {
            ft.replace(viewId, fragment, tag);
        }
//		ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
    }

    /**
     * Replace the fragment to the activity
     * @param fragment
     */
    public void replaceWithAnimation(Fragment fragment) {
        replaceWithAnimation(fragment, null);
    }

    /**
     * Replace the fragment to the activity
     * @param fragment
     * @param tag
     */
    public void replaceWithAnimation(Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.enter_replace, R.anim.exit_replace);
        if (TextUtils.isEmpty(tag)) {
            ft.replace(viewId, fragment);
        } else {
            ft.replace(viewId, fragment, tag);
        }
        ft.commitAllowingStateLoss();
    }


    /**
     * Remove the fragment to the activity
     */
    public void remove() {
        remove(fragmentManager.findFragmentById(viewId));
    }

    /**
     * Remove the fragment to the activity
     * @param tag
     */
    public void remove(String tag) {
        remove(fragmentManager.findFragmentByTag(tag));
    }

    /**
     * Remove the fragment to the activity
     * @param fragment
     */
    public void remove(Fragment fragment) {
        if (fragment != null) {
//			fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }

    /**
     * Remove the fragment to the activity
     */
    public void removeWithAnimation() {
        removeWithAnimation(fragmentManager.findFragmentById(viewId));
    }

    /**
     * Remove the fragment to the activity
     * @param fragment
     */

    public void removeWithAnimation(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_remove, R.anim.exit_remove);
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }

    public void popBackStack() {
        fragmentManager.popBackStackImmediate();
    }

    /**
     * Action back key
     */
    public boolean onBackPressed() {
        boolean isResult = false;
        Fragment fragment = fragmentManager.findFragmentById(viewId);
        if (fragment != null && fragment instanceof BaseFragment) {
            isResult = ((BaseFragment) fragment).onBackPressed();
            if (isResult == false) {
                ((BaseFragment) fragment).finish();
            }
        }
        return isResult;
    }

    public boolean onHomeBackPressed() {
        boolean isResult = false;
        Fragment fragment = fragmentManager.findFragmentById(viewId);
        if (fragment != null && fragment instanceof BaseFragment) {
            isResult = ((BaseFragment) fragment).onBackPressed();
        }
        return isResult;
    }

    /**
     * lifecycle resume
     */
    public void onResume() {
        Fragment fragment = fragmentManager.findFragmentById(viewId);
        if (fragment != null && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onResume();
        }
    }

    public void onPause() {
        Fragment fragment = fragmentManager.findFragmentById(viewId);
        if (fragment != null && fragment instanceof BaseFragment) {
            ((BaseFragment) fragment).onPause();
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean isResult = false;
        Fragment fragment = fragmentManager.findFragmentById(viewId);
        if (fragment != null && fragment instanceof BaseFragment) {
            isResult = ((BaseFragment) fragment).dispatchTouchEvent(ev);
        }
        return isResult;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean isResult = false;
        Fragment fragment = fragmentManager.findFragmentById(viewId);
        if (fragment != null && fragment instanceof BaseFragment) {
            isResult = ((BaseFragment) fragment).onTouchEvent(ev);
        }
        return isResult;
    }

    /**
     * Get the fragment by tag
     * @param tag
     * @return
     */
    public BaseFragment getFragmentByTag(String tag) {
        return (BaseFragment) fragmentManager.findFragmentByTag(tag);
    }

    /**
     * Get the fragment by id
     * @return
     */
    public BaseFragment getFragmentById() {
        return (BaseFragment)fragmentManager.findFragmentById(viewId);
    }

}
