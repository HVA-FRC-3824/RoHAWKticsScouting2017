package frc3824.rohawkticsscouting2017.Fragments.MatchPlanning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Date;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Utilities.Utilities;
import frc3824.rohawkticsscouting2017.Views.StrategyDrawingView;

/**
 * @author frc3824
 * Created: 12/22/16
 */
public class SinglePlanFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "SinglePlanFragment";

    private StrategyDrawingView mDrawingView;
    private EditText mNotes;
    private float mExtraSmallBrush, mSmallBrush, mMediumBrush, mLargeBrush;
    private ArrayList<Integer> mTeams;

    public SinglePlanFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_single_plan, container, false);

        mDrawingView = (StrategyDrawingView) view.findViewById(R.id.drawing);

        mExtraSmallBrush = 5;
        mSmallBrush = 10;
        mMediumBrush = 20;
        mLargeBrush = 30;

        view.findViewById(R.id.draw_btn).setOnClickListener(this);
        view.findViewById(R.id.erase_btn).setOnClickListener(this);
        view.findViewById(R.id.new_btn).setOnClickListener(this);

        mDrawingView.setBrushSize(mExtraSmallBrush);

        Integer[] teamArray = mTeams.toArray(new Integer[mTeams.size()]);
        mDrawingView.markTeams(teamArray, Constants.StrategyPlanning.Auto_Endgame.POSITIONS, Constants.StrategyPlanning.Auto_Endgame.RADIUS);

        mNotes = (EditText)view.findViewById(R.id.notes);

        Utilities.setupUi(getActivity(), view);

        return view;
    }

    public void setMatch(ArrayList<Integer> teams){
        mTeams = teams;
    }

    public Strategy save() {
        Strategy strategy = new Strategy();
        strategy.updated = new Date().getTime();
        strategy.path_json = mDrawingView.toJson();
        strategy.notes = mNotes.getText().toString();
        return strategy;
    }

    public void load(Strategy strategy){
        mDrawingView.startNew();
        mDrawingView.load(strategy.path_json);
        mNotes.setText(strategy.notes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.draw_btn:
                drawButtonClicked();
                break;
            case R.id.erase_btn:
                eraseButtonClicked();
                break;
            case R.id.new_btn:
                newButtonClicked();
                break;
        }
    }

    private void drawButtonClicked() {
        mDrawingView.setErase(false);
        final Dialog brushDialog = new Dialog(getContext());
        brushDialog.setTitle("Brush size:");
        brushDialog.setContentView(R.layout.dialog_brush_chooser);

        // extra small button sets the size of the brush to extra small
        ImageButton extraSmallBtn = (ImageButton) brushDialog.findViewById(R.id.extra_small_brush);
        extraSmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mExtraSmallBrush);
                mDrawingView.setLastBrushSize(mExtraSmallBrush);
                brushDialog.dismiss();
            }
        });

        // small button sets the size of the brush to small
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mSmallBrush);
                mDrawingView.setLastBrushSize(mSmallBrush);
                brushDialog.dismiss();
            }
        });

        // medium button sets the size of the brush to medium
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mMediumBrush);
                mDrawingView.setLastBrushSize(mMediumBrush);
                brushDialog.dismiss();
            }
        });

        // large button sets the size of the brush to large
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mLargeBrush);
                mDrawingView.setLastBrushSize(mLargeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }

    private void newButtonClicked() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(getContext());
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new strategy (you will lose the current strategy)?");

        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mDrawingView.startNew();
                dialog.dismiss();
            }
        });

        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        newDialog.show();
    }

    private void eraseButtonClicked() {
        final Dialog eraser_brushDialog = new Dialog(getContext());
        eraser_brushDialog.setTitle("Eraser size:");
        eraser_brushDialog.setContentView(R.layout.dialog_brush_chooser);

        // extra small button sets the size of the eraser brush to extra small
        ImageButton eraser_extraSmallBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.extra_small_brush);
        eraser_extraSmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mExtraSmallBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // small button sets the size of the eraser brush to small
        ImageButton eraser_smallBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.small_brush);
        eraser_smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mSmallBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // medium button sets the size of the eraser brush to medium
        ImageButton eraser_mediumBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.medium_brush);
        eraser_mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mMediumBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // large button sets the size of the eraser brush to large
        ImageButton eraser_largeBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.large_brush);
        eraser_largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mLargeBrush);
                eraser_brushDialog.dismiss();
            }
        });
        eraser_brushDialog.show();
    }
}
