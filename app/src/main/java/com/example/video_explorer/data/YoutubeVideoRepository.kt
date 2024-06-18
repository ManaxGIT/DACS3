package com.example.video_explorer.data

import android.util.Log
import com.example.video_explorer.data.youtubeData.VideoAddtitionalDetails
import com.example.video_explorer.data.youtubeData.YoutubeChannel
import com.example.video_explorer.data.youtubeData.YoutubeVideo
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment
import com.example.video_explorer.data.youtubeData.parts.VideoStatisticsResponse
import com.example.video_explorer.network.YoutubeApiService
import com.example.video_explorer.ui.render.getDateAgo


interface YoutubeVideoRepository {
    suspend fun getAdditionalDetails(videoId: String): VideoAddtitionalDetails
    suspend fun getVideoStatistics(videoId: String): VideoStatisticsResponse
    suspend fun getChannelDetails(channelId: String): YoutubeChannel
    suspend fun getVideoCommentList(videoId: String): YoutubeVideoComment
    suspend fun getSearchVideo(query: String, maxResult: Int, order: String?, type: String?, date: String?, length: String?, nextPageToken: String?): YoutubeVideo
    suspend fun rateVideo(videoId: String, rating: String)
}

class NetworkYoutubeVideoRepository(
    val youtubeApiService: YoutubeApiService
): YoutubeVideoRepository {
    override suspend fun getAdditionalDetails(videoId: String): VideoAddtitionalDetails {
        return youtubeApiService.getAdditionalDetails(videoId = videoId)
    }

    override suspend fun getVideoStatistics(videoId: String): VideoStatisticsResponse {
        return youtubeApiService.getVideoStatistics(videoId = videoId)
    }

    override suspend fun getChannelDetails(channelId: String): YoutubeChannel {
        return youtubeApiService.getChannelDetails(channelId = channelId)
    }

    override suspend fun getVideoCommentList(videoId: String): YoutubeVideoComment {
        return youtubeApiService.getVideoCommentList(videoId = videoId)
    }

    override suspend fun getSearchVideo(query: String, maxResult: Int, order: String?, type: String?, date: String?, length: String?, nextPageToken: String?): YoutubeVideo {
        val newOrder = when(order) {
            "Mức độ liên quan" -> "relevance"
            "Ngày tải lên" -> "date"
            "Lượt xem" -> "viewCount"
            "Xếp hạng" -> "rating"
            else -> "relevance"
        }

        val newType = when(type) {
            else -> "video"
        }

        val newDate  = when(date) {
            "Mọi thời điểm" -> "1970-01-01T00:00:00Z"
            "Một giờ qua" -> getDateAgo(60*60*1000)
            "Hôm nay" -> getDateAgo(24*60*60*1000)
            "Tuần này" -> getDateAgo(7*24*60*60*1000)
            "Tháng này" -> getDateAgo(30L*24*60*60*1000)
            "Năm nay" -> getDateAgo(365L*24*60*60*1000)
            else -> "1970-01-01T00:00:00Z"
        }

        val newLength = when(length) {
            "Dưới 4 phút" -> "short"
            "4 - 20 phút" -> "medium"
            "Trên 20 phút" -> "long"
            else -> "any"
        }

        Log.i("ex_search", "query=$query order=$order type=$type date=$date length=$length")
        Log.i("ex_search", "query=$query order=$newOrder type=$newType date=$newDate length=$newLength")

        return youtubeApiService.getSearchVideo(
            query = query,
            apiKey = "AIzaSyBimcL3JIAot4qN5qvbKf_e1_AXljs6Luo",
            maxResults = maxResult.toString(),
            order = newOrder,
            type = newType,
            publishedAfter = newDate,
            videoDuration = newLength,
            pageToken = nextPageToken
        )
    }

    override suspend fun rateVideo(videoId: String, rating: String) {
        return youtubeApiService.rateVideo(videoId = videoId, rating = rating)
    }
}
