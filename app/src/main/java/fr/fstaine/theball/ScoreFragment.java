package fr.fstaine.theball;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ScoreFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PREV_SCORE = "PrevScore";

    private int mPrevScore;

    private OnScoreFragmentInteractionListener mListener;

    private Button mBtRestart;

    public ScoreFragment() {
        // Required empty public constructor
    }

    public static ScoreFragment newInstance(int prevScore) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PREV_SCORE, prevScore);
        fragment.setArguments(args);
        return fragment;
    }

    public static ScoreFragment newInstance() {
        return new ScoreFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPrevScore = getArguments().getInt(ARG_PREV_SCORE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_score, container, false);

        mBtRestart = root.findViewById(R.id.btRestart);
        mBtRestart.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btRestart:
                if (mListener != null) {
                    mListener.onStartGame();
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScoreFragmentInteractionListener) {
            mListener = (OnScoreFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScoreFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnScoreFragmentInteractionListener {
        void onStartGame();
    }
}
