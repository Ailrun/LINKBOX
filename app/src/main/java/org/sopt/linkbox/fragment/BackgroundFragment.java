package org.sopt.linkbox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.sopt.linkbox.R;

/**
 * Created by sy on 2015-09-02.
 */
public class BackgroundFragment extends Fragment{

    private static final String PAGE_COUNT = "count";

    private int mBackground;


    public static BackgroundFragment newInstance(int param1) {
        BackgroundFragment fragment = new BackgroundFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_COUNT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public BackgroundFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            switch (getArguments().getInt(PAGE_COUNT))
            {
                case 0:
                    mBackground = R.drawable.tutorial_image_01;
                    break;
                case 1:
                    mBackground = R.drawable.tutorial_image_02;
                    break;
                case 2:
                    mBackground = R.drawable.tutorial_image_03;
                    break;
                case 3:
                    mBackground = R.drawable.tutorial_image_04;
                    break;

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_fragment, container, false);

        v.setBackgroundResource(mBackground);

        return v;
    }
}
