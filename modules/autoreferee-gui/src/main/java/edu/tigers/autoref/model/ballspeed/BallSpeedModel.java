/*
 * Copyright (c) 2009 - 2018, DHBW Mannheim - TIGERs Mannheim
 */
package edu.tigers.autoref.model.ballspeed;

import edu.tigers.sumatra.math.vector.IVector3;
import edu.tigers.sumatra.referee.data.GameState;
import edu.tigers.sumatra.wp.data.KickedBall;
import edu.tigers.sumatra.wp.data.WorldFrameWrapper;


/**
 * @author "Lukas Magel"
 */
public class BallSpeedModel
{
    private double lastBallSpeed = 0.0d;
    private double lastEstimatedBallSpeed = 0.0d;

    private GameState lastState = GameState.HALT;
    private boolean gameStateChanged = false;

    // 前フレームで KickedBall が存在したかどうか（キック検出用）
    private boolean hadKickedBallLastFrame = false;


    /**
     * @param wFrameWrapper
     */
    public void update(final WorldFrameWrapper wFrameWrapper)
    {
        GameState curState = wFrameWrapper.getGameState();

        if (!curState.equals(lastState))
        {
            gameStateChanged = true;
        }

        // 現在のボール速度
        lastBallSpeed = wFrameWrapper.getSimpleWorldFrame().getBall().getVel3().getLength();

        var swf = wFrameWrapper.getSimpleWorldFrame();

        boolean hasKickedBall = swf.getKickedBall().isPresent();

        if (hasKickedBall && !hadKickedBallLastFrame)
        {
            // 新しいキックを検出（立ち上がりエッジ）
            KickedBall kickedBall = swf.getKickedBall().get();
            lastEstimatedBallSpeed = kickedBall.getKickVel().getLength();

            // ここで1キックにつき1回のみ出力
            System.out.println("Estimated Kick Initial Speed: " + lastEstimatedBallSpeed);
        }
        else if (!hasKickedBall)
        {
            // lastEstimatedBallSpeed = 0.0;
        }

        // 次フレーム用に記録
        hadKickedBallLastFrame = hasKickedBall;
        // ==========================================================


        lastState = curState;
    }


    /**
     *
     */
    public void reset()
    {
        gameStateChanged = false;
    }


    /**
     * @return
     */
    public double getLastBallSpeed()
    {
        return lastBallSpeed;
    }


    /**
     * @return
     */
    public GameState getLastState()
    {
        return lastState;
    }


    /**
     * @return
     */
    public boolean hasGameStateChanged()
    {
        return gameStateChanged;
    }


    public double getLastEstimatedBallSpeed()
    {
        return lastEstimatedBallSpeed;
    }
}

