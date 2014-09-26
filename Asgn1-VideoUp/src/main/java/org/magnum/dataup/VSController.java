/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.magnum.dataup.VideoSvcApi.*;

@Controller
public class VSController {

    Map<Long, Video> videos = new HashMap<Long, Video>();
    @Autowired
    VideoFileManager manager;


    @RequestMapping(VIDEO_SVC_PATH)
    public @ResponseBody Collection<Video> getVideoList() {
        return videos.values();
    }

    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video v) {

        long id = (long) (videos.size() + 1);
        v.setId(id);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String url = "http://" + request.getServerName()
                + ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "") + "/video/" + id + "/data";
        v.setDataUrl(url);
        videos.put(id, v);
        return v;
    }

    @RequestMapping(value = VIDEO_DATA_PATH, method = RequestMethod.POST)
    public @ResponseBody VideoStatus setVideoData(@PathVariable(ID_PARAMETER) long id,
                                                  @RequestParam(DATA_PARAMETER) MultipartFile data,
                                                  HttpServletResponse response) throws IOException {

        if (!videos.containsKey(id)) {
            response.setStatus(404);
            return null;
        }
        manager.saveVideoData(videos.get(id), data.getInputStream());

        return new VideoStatus(VideoStatus.VideoState.READY);
    }

    @RequestMapping(VIDEO_DATA_PATH)
    public void getData(@PathVariable(ID_PARAMETER) long id,
                        HttpServletResponse response) throws IOException {

        if (!videos.containsKey(id) ) {
            response.setStatus(404);
        } else {
            manager.copyVideoData(videos.get(id), response.getOutputStream());
        }
    }
}
