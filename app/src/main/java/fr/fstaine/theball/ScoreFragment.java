package fr.fstaine.theball;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.fstaine.theball.pref.AppPreferences;


public class ScoreFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ScoreFragment";

    private static final String ARG_PREV_SCORE = "PrevScore";

    private OnScoreFragmentInteractionListener mListener;

    private Button mBtRestart;
    private ListView mListHighScore;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_score, container, false);

        mBtRestart = root.findViewById(R.id.bt_restart);
        mBtRestart.setOnClickListener(this);

        if (getArguments() != null) {
            TextView textScore = root.findViewById(R.id.text_score);
            int score = getArguments().getInt(ARG_PREV_SCORE);
            textScore.setText(Integer.toString(score));
        }

        mListHighScore = root.findViewById(R.id.list_highscore);

        final List<Integer> highScores = new ArrayList<>(AppPreferences.getHighScore(getContext()));
        Collections.sort(highScores, Collections.<Integer>reverseOrder());
        final List<String> highScoresStr = new ArrayList<>();
        for (Integer i : highScores) {
            highScoresStr.add(Integer.toString(i));
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), R.layout.list_item_highscore, highScoresStr);
        mListHighScore.setAdapter(adapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_restart:
                if (mListener != null) {
                    mListener.onStartGame();
                }
                break;
        }
    }

    public interface OnScoreFragmentInteractionListener {
        void onStartGame();
    }
}
