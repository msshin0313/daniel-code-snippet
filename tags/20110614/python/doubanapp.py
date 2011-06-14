# coding=utf8
'''
Created on Aug 1, 2010
Application for Douban
@author: Daniel
'''

import douban.service
from pprint import pprint

APIKEY = '001b380cc8e913370051a8265abb39a5'
SECRET = ''
DANIEL = '1115547'

client = douban.service.DoubanService(api_key=APIKEY)
#feed = client.SearchMovie("Monty Python")
#for movie in feed.entry:
#    print "%s: %s" % (movie.title.text, movie.GetAlternateLink().href)

