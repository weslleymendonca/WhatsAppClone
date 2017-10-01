package com.weslleymendonca.whatsapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.weslleymendonca.whatsapp.fragmento.ContatosFragments;
import com.weslleymendonca.whatsapp.fragmento.ConversasFragment;

/**
 * Created by usuario on 02/09/17.
 */

public class TabAdapter extends FragmentStatePagerAdapter {


    private String[] tituloAbas = {"CONVERSAS", "CONTATOS"};


    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position){
            case 0 :
                fragment = new ConversasFragment();
                break;

            case 1 :
                fragment = new ContatosFragments();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return tituloAbas[position];

    }
}
