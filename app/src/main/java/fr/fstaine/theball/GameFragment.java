package fr.fstaine.theball;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fr.fstaine.theball.controller.GameEngine;
import fr.fstaine.theball.pref.AppPreferences;
import fr.fstaine.theball.view.GameView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameFragment.OnGameFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment implements View.OnClickListener, GameEngine.OnGameEventListener {
    private static final String TAG = "GameFragment";

    private static final String ARG_GAME_DIFFICULTY = "gameDifficulty";

    OnGameFragmentInteractionListener mListener;

    GameEngine gameEngine;

    GameView gameView;
    TextView mTextScore, mTextTimer;
    View startLayout, gameLayout;

    public GameFragment() {
        // Required empty constructor
    }

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGameFragmentInteractionListener) {
            mListener = (OnGameFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement " + OnGameFragmentInteractionListener.class);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game, container, false);

        mTextScore = root.findViewById(R.id.text_score);
        mTextTimer = root.findViewById(R.id.text_timer);
        gameView = root.findViewById(R.id.game_view);
        startLayout = root.findViewById(R.id.start_layout);
        gameLayout = root.findViewById(R.id.game_layout);

        Button btEasy = root.findViewById(R.id.bt_easy);
        Button btMedium = root.findViewById(R.id.bt_medium);
        Button btHard = root.findViewById(R.id.bt_hard);

        btEasy.setOnClickListener(this);
        btMedium.setOnClickListener(this);
        btHard.setOnClickListener(this);

        gameEngine = new GameEngine(this, gameView);

        return root;
    }

    public void updateScore(final int score) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextScore.setText(Integer.toString(score));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_easy:
                launchGame(GameEngine.GameLevel.EASY);
                break;
            case R.id.bt_medium:
                launchGame(GameEngine.GameLevel.MEDIUM);
                break;
            case R.id.bt_hard:
                launchGame(GameEngine.GameLevel.HARD);
                break;
        }
    }

    @Override
    public void onTimerChanged(int msRemaining) {
        mTextTimer.setText(String.format("%.1f", msRemaining / 1000f));
    }

    @Override
    public void onGameEnd(int playerScore) {
        AppPreferences.updateHighScore(getContext(), playerScore);
        if (mListener != null) {
            mListener.onGameEnd(playerScore);
        }
    }

    public void launchGame(int difficulty) {
        gameEngine = new GameEngine(this, gameView);
        startLayout.setVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.VISIBLE);
        gameEngine.start(difficulty);
    }

    public interface OnGameFragmentInteractionListener {
        void onGameEnd(int playerScore);
        // ...
    }

}
