
from django.conf.urls import url
from namenode import views

urlpatterns = [
    url(r'^register/$', views.Registration.as_view()),
    url(r'^heartbeat/$', views.Heartbeat.as_view()),
    url(r'^finalize/$', views.Finalize.as_view()),
    url(r'^write/$', views.File.as_view()),
    url(r'^read/', views.File.as_view()),
    url(r'^delete/', views.File.as_view())
]
