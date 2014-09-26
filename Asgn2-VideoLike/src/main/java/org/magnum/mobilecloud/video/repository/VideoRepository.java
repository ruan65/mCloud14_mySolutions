package org.magnum.mobilecloud.video.repository;

import org.magnum.mobilecloud.video.client.VideoSvcApi;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;


@RepositoryRestResource(path = VideoSvcApi.VIDEO_SVC_PATH)
public interface VideoRepository extends CrudRepository<Video, Long> {

    Collection<Video> findByName(@Param(VideoSvcApi.TITLE_PARAMETER) String title);

    Collection<Video> findByDurationLessThan(@Param(VideoSvcApi.DURATION_PARAMETER) long maxduration);
}
