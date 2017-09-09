package fr.fstaine.theball;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.fstaine.theball.controller.GameEngine;
import fr.fstaine.theball.view.GameView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    private static final String TAG = "GameFragment";

    private static final String ARG_GAME_DIFFICULTY = "gameDifficulty";
    OnGameFragmentInteractionListener mListener;
    GameEngine gameEngine;
    GameView gameView;
    TextView mTextScore;

    public GameFragment() {
        // Required empty constructor
    }

    public static GameFragment newInstance(int difficulty) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GAME_DIFFICULTY, difficulty);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int difficulty;
        if (getArguments() != null) {
            difficulty = getArguments().getInt(ARG_GAME_DIFFICULTY);
        }
        // setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game, container, false);

        mTextScore = root.findViewById(R.id.textScore);
        gameView = root.findViewById(R.id.gameView);

        gameEngine = new GameEngine(this, gameView.getBall(), gameView.getBonus());

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        gameEngine.resume();
        gameEngine.updateGameParams();
    }

    @Override
    public void onPause() {
        super.onPause();
        gameEngine.stop();
    }

    public void updateScore(final int score) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextScore.setText("" + score);
            }
        });
    }

    public interface OnGameFragmentInteractionListener {
        void onGameStart();

        void onGamePause();

        void onGameResume();

        void onGameEnd();

        void onBonusCaught(int reward);
        // ...
    }

}
