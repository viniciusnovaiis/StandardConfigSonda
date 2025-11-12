package br.com.pdasolucoes.standardconfig.enums;

import androidx.fragment.app.Fragment;

public class FragmentTag {


    private String tag;
    private Class<? extends Fragment> fragmentClass;

    public FragmentTag(String tag, Class<? extends Fragment> fragmentClass) {
        this.tag = tag;
        this.fragmentClass = fragmentClass;
    }

    public String getTag() {
        return this.tag;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return this.fragmentClass;
    }
}
