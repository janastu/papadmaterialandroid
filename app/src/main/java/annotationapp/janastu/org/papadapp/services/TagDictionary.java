/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package annotationapp.janastu.org.papadapp.services;


import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import annotationapp.janastu.org.papadapp.rest.AudioAnnotationFile;
import annotationapp.janastu.org.papadapp.rest.JsonRestData;

public class TagDictionary
{
    private static String LOGGER = "TagDictionary";
    static JsonRestData data;

    public   ArrayList<String> tagsDirectory = new ArrayList<String>();


    public TagDictionary(JsonRestData data) {
        this.data = data;


    }

    public   ArrayList<String> getTagsDirectory() {

        List<AudioAnnotationFile> files = data.getFiles();
        for(AudioAnnotationFile t: files)
        {
            List<String> tTags =      t.getTags();
            for(String t1 : tTags)
            {
                boolean found = tagsDirectory.contains(t1);
                if(found == false)// if not found add to list;
                    tagsDirectory.add(t1);
            }
        }
        return tagsDirectory;
    }



    public static List<AudioAnnotationFile>  findTag(String tag)
    {

        Log.d(LOGGER, "finding tag"+tag);

        List<AudioAnnotationFile> tagedAnnotationList = new ArrayList<AudioAnnotationFile>();
        AudioAnnotationFile tagedAnnotation;
        List<AudioAnnotationFile> files = data.getFiles();
        for(AudioAnnotationFile tAnnotFile: files)
        {
            List<String> tTags =      tAnnotFile.getTags();

            if(tTags.contains(tag))
            {
                tagedAnnotationList.add(tAnnotFile);
                Log.d(LOGGER, "found tag at FILE ID"+tAnnotFile.getId());

                Log.d(LOGGER, "found tag at FILE ID"+tAnnotFile.getUrl());
            }
        }
        return tagedAnnotationList;
    }

    public TagDictionary() {
    }

}
