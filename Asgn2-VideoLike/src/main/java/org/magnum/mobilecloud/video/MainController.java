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

package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.repository.Video;
import org.magnum.mobilecloud.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Set;

import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_SVC_PATH;

@Controller
public class MainController {

    @Autowired
    private ApplicationContext ctx;
    private VideoRepository repo;

    @PostConstruct
    public void init() {
        repo = ctx.getBean(VideoRepository.class);
    }

    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video video) {
        return repo.save(video);
    }

    @RequestMapping(VIDEO_SVC_PATH)
    public @ResponseBody Iterable<Video> getAllVideo() {
        return repo.findAll();
    }

    @RequestMapping("video/{id}")
    public @ResponseBody Video getVideoById(@PathVariable("id") long id, HttpServletResponse response) {

        Video v = repo.findOne(id);
        if (v == null) {
            response.setStatus(404);
            return null;
        }
        return v;
    }

    @RequestMapping(value = "video/{id}/like", method = RequestMethod.POST)
    public void likeVideo(@PathVariable("id") long id, Principal user, HttpServletResponse response) {

        Video v = repo.findOne(id);
        if (v == null) {
            response.setStatus(404);
        } else if (!v.like(user.getName())) {
            response.setStatus(400);
        } else repo.save(v);
    }

    @RequestMapping(value = "video/{id}/unlike", method = RequestMethod.POST)
    public void unlikeVideo(@PathVariable("id") long id, Principal user, HttpServletResponse response) {

        Video v = repo.findOne(id);
        if (v == null) {
            response.setStatus(404);
        } else if (!v.unLike(user.getName())) {
            response.setStatus(400);
        } else repo.save(v);
    }

    @RequestMapping("video/{id}/likedby")
    public @ResponseBody Set<String> likedBy(@PathVariable("id") long id, HttpServletResponse response) {

        Video v = repo.findOne(id);
        if (v == null) {
            response.setStatus(404);
            return null;
        }
        return v.getUsersLikeIt();
    }
}
