package com.nicktoony.getaway.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;
import com.uwsoft.editor.renderer.resources.FontSizePair;
import com.uwsoft.editor.renderer.resources.ResourceManager;

/**
 * Created by Nick on 09/09/2015.
 */
public class BetterResourceManager extends ResourceManager {

    protected String rootPath;

    public BetterResourceManager(String rootPath) {
        this.rootPath = rootPath;
        packResolutionName = rootPath + packResolutionName;
        scenesPath = rootPath + scenesPath;
        particleEffectsPath = rootPath + particleEffectsPath;
        spriteAnimationsPath = rootPath + spriteAnimationsPath;
        spriterAnimationsPath = rootPath + spriterAnimationsPath;
        spineAnimationsPath = rootPath + spineAnimationsPath;
        fontsPath = rootPath + fontsPath;
        shadersPath = rootPath + shadersPath;

        initAllResources();
    }

    @Override
    public ProjectInfoVO loadProjectVO() {

        FileHandle file = Gdx.files.internal(rootPath + "project.dt");
        Json json = new Json();
        projectVO = json.fromJson(ProjectInfoVO.class, file.readString());

        return projectVO;
    }

    @Override
    public void loadFonts() {
        //resolution related stuff
        ResolutionEntryVO curResolution = getProjectVO().getResolution(packResolutionName);
        resMultiplier = 1;
        if(!packResolutionName.equals(rootPath + "orig")) {
            if(curResolution.base == 0) {
                resMultiplier = (float) curResolution.width / (float) getProjectVO().originalResolution.width;
            } else{
                resMultiplier = (float) curResolution.height / (float) getProjectVO().originalResolution.height;
            }
        }

        // empty existing ones that are not scheduled to load
        for (FontSizePair pair : bitmapFonts.keySet()) {
            if (!fontsToLoad.contains(pair)) {
                bitmapFonts.remove(pair);
            }
        }

        for (FontSizePair pair : fontsToLoad) {
            loadFont(pair);
        }
    }

    @Override
    public ResolutionEntryVO getLoadedResolution() {
        if(packResolutionName.equals(rootPath + "orig")) {
            return getProjectVO().originalResolution;
        }
        return getProjectVO().getResolution(packResolutionName);
    }
}
